FROM public.ecr.aws/lambda/java:11
COPY build/libs/bookkeeper-pipeline-trigger-all.jar ${LAMBDA_TASK_ROOT}
CMD ["ro.jf.bk.infra.bookkeeper.infra.pipelinetrigger.BookkeeperPipelineTrigger::handleRequest"]
