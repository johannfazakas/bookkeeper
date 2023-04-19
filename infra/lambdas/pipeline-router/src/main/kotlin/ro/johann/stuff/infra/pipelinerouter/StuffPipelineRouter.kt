package ro.johann.stuff.infra.pipelinerouter

import com.amazonaws.services.codepipeline.AWSCodePipelineClientBuilder
import com.amazonaws.services.codepipeline.model.StartPipelineExecutionRequest
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.fasterxml.jackson.databind.ObjectMapper

class StuffPipelineRouter : RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private val codePipelineClient = AWSCodePipelineClientBuilder.defaultClient()
    private val objectMapper = ObjectMapper()

    override fun handleRequest(input: APIGatewayProxyRequestEvent, context: Context): APIGatewayProxyResponseEvent {
        val logger = context.logger
        val pipelineName = "stuff-web-app-code-pipeline"
        logger.log("Will trigger pipeline $pipelineName. Input: ${input.body}")

        val result = codePipelineClient.startPipelineExecution(StartPipelineExecutionRequest().withName(pipelineName))
        val response = PipelineRoutingResponse(pipelineName, result.pipelineExecutionId)

        logger.log("Pipeline $pipelineName triggered successfully. Execution id: ${result.pipelineExecutionId}")

        return APIGatewayProxyResponseEvent()
            .withStatusCode(200)
            .withHeaders(mapOf("Content-Type" to "application/json"))
            .withBody(objectMapper.writeValueAsString(response))
    }
}

private data class PipelineRoutingResponse(
    val pipelineName: String,
    val executionId: String,
)
