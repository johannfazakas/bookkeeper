FROM public.ecr.aws/lambda/java:11
COPY build/libs/bookkeeper-pipeline-trigger-all.jar ${LAMBDA_TASK_ROOT}/lib/
CMD ["ro.johann.bookkeeper.infra.pipelinetrigger.BookkeeperPipelineTrigger::handleRequest"]
