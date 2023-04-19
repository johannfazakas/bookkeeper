package ro.johann.stuff.infra.pipelinerouter

import com.amazonaws.services.codepipeline.AWSCodePipelineClientBuilder
import com.amazonaws.services.codepipeline.model.StartPipelineExecutionRequest
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

class StuffPipelineRouter : RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private companion object {
        private val codePipelineClient = AWSCodePipelineClientBuilder.defaultClient()
        private val objectMapper = ObjectMapper()
        private val services = listOf(
            StuffService(
                name = "stuff-web-app",
                pipelineName = "stuff-web-app-code-pipeline",
                rootFolder = "web-app/"
            )
        )
    }

    override fun handleRequest(input: APIGatewayProxyRequestEvent, context: Context): APIGatewayProxyResponseEvent {
        val pushEvent = objectMapper.readValue<GitHubPushEvent>(input.body)
        context.logger.log("Pipeline router triggered by push event: $pushEvent.")
        val changes = pushEvent.commits.asSequence()
            .flatMap(GitHubCommit::changedFiles)
            .toSet()

        val pipelineExecutions = services
            .filter { service -> changes.any { it.startsWith(service.rootFolder) } }
            .map {
                context.logger.log("Service ${it.name} modified. Triggering pipeline ${it.pipelineName}.")
                val startPipelineExecution =
                    codePipelineClient.startPipelineExecution(StartPipelineExecutionRequest().withName(it.pipelineName))
                PipelineExecution(it.pipelineName, startPipelineExecution.pipelineExecutionId)
            }
        context.logger.log("Pipeline router finished. Triggered pipelines: $pipelineExecutions.")

        return APIGatewayProxyResponseEvent()
            .withStatusCode(200)
            .withHeaders(mapOf("Content-Type" to "application/json"))
            .withBody(objectMapper.writeValueAsString(PipelineRoutingResponse(pipelineExecutions)))
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
private data class GitHubPushEvent @JsonCreator constructor(
    @JsonProperty("ref") val ref: String,
    @JsonProperty("before") val before: String,
    @JsonProperty("after") val after: String,
    @JsonProperty("repository") val repository: GitHubRepository,
    @JsonProperty("commits") val commits: List<GitHubCommit>,
)

@JsonIgnoreProperties(ignoreUnknown = true)
private data class GitHubRepository @JsonCreator constructor(
    @JsonProperty("id") val id: Long,
    @JsonProperty("name") val name: String,
)

@JsonIgnoreProperties(ignoreUnknown = true)
private data class GitHubCommit @JsonCreator constructor(
    @JsonProperty("id") val id: String,
    @JsonProperty("modified") val modifiedFiles: List<String>,
    @JsonProperty("added") val addedFiles: List<String>,
    @JsonProperty("removed") val removedFiles: List<String>,
) {
    val changedFiles = modifiedFiles + addedFiles + removedFiles
}

private data class StuffService(
    val name: String,
    val rootFolder: String,
    val pipelineName: String,
)

private data class PipelineRoutingResponse(
    val pipelinesStarted: List<PipelineExecution>
)

private data class PipelineExecution(
    val pipelineName: String,
    val executionId: String
)
