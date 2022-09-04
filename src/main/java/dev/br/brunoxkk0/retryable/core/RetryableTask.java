package dev.br.brunoxkk0.retryable.core;

public abstract class RetryableTask<T> implements IRetryableTask {

    private TaskState currentState = TaskState.WAITING;
    private int currentAttempt = 0;
    private String name = "";

    protected RetryableTaskExecutor retryableTaskExecutorInstance;

    public RetryableTask(String name){
        this.name = name;
    }

    public RetryableTask(){}

    public void updateState(TaskState currentState) {
        this.currentState = currentState;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public TaskState getState() {
        return currentState;
    }

    public void setRetryableTaskExecutorInstance(RetryableTaskExecutor retryableTaskExecutorInstance) {
        this.retryableTaskExecutorInstance = retryableTaskExecutorInstance;
    }

    @Override
    public int getAttempt() {
        return currentAttempt;
    }

    public void requeueTask(RetryableTaskExecutor retryableTaskExecutor){
        this.currentAttempt++;
        retryableTaskExecutor.queue(this);
    }

    @Override
    public void run() {
        try{
            T returnObject = doLogic();
            onDone(returnObject);
        }catch (Exception e){
            if(currentAttempt >= retryableTaskExecutorInstance.getMaxAttempts()){
                onError(e);
            }else {
                requeueTask(retryableTaskExecutorInstance);
            }
        }
    }

    public abstract T doLogic() throws Exception;

    public abstract void onDone(T object);

    public abstract <E extends Exception> void onError( E exception);

}
