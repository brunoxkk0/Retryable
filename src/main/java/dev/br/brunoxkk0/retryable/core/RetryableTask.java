package dev.br.brunoxkk0.retryable.core;

public abstract class RetryableTask implements IRetryableTask{

    private TaskState currentState = TaskState.WAITING;
    private int currentAttempt = 0;

    public void updateState(TaskState currentState) {
        this.currentState = currentState;
    }

    public TaskState getState() {
        return currentState;
    }

    @Override
    public int getAttempt() {
        return currentAttempt;
    }

    public void requeueTask(RetryableTaskExecutor retryableTaskExecutor){
        this.currentAttempt++;
        retryableTaskExecutor.queue(this);
    }
}
