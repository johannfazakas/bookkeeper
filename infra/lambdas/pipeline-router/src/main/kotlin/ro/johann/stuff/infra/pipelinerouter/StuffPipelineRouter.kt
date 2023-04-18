package ro.johann.stuff.infra.pipelinerouter

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent

class StuffPipelineRouter: RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    override fun handleRequest(input: APIGatewayProxyRequestEvent, context: Context): APIGatewayProxyResponseEvent {
        val logger = context.logger
        logger.log("Received request with body: ${input.body}")

        return APIGatewayProxyResponseEvent()
            .withStatusCode(200)
            .withBody("Response from Kotlin Lambda")
    }
}
