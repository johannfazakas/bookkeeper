FROM public.ecr.aws/lambda/java:11
COPY build/libs/stuff-pipeline-router-all.jar ${LAMBDA_TASK_ROOT}/lib/
CMD ["ro.johann.stuff.infra.pipelinerouter.StuffPipelineRouter::handleRequest"]
