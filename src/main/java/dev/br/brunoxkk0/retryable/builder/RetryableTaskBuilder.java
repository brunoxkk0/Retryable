package dev.br.brunoxkk0.retryable.builder;

import dev.br.brunoxkk0.retryable.core.RetryableTask;
import dev.br.brunoxkk0.retryable.core.RetryableTaskExecutor;
import dev.br.brunoxkk0.retryable.function.Execute;

import java.util.function.Consumer;

public class RetryableTaskBuilder<T> {

    private Execute<T> doLogic;
    private Consumer<RetryableTaskReturn<T>> onDone;
    private Consumer<RetryableTaskError> onError;
    private String name;

    public static <T> RetryableTaskBuilder<T> newBuilder(){
        return new RetryableTaskBuilder<T>();
    }

    private RetryableTaskBuilder(){}

    public RetryableTaskBuilder<T> of(Execute<T> doLogic){
        this.doLogic = doLogic;
        return this;
    }

    public RetryableTaskBuilder<T> named(String name){
        this.name = name;
        return this;
    }

    public RetryableTaskBuilder<T> done(Consumer<RetryableTaskReturn<T>> onDone){
        this.onDone = onDone;
        return this;
    }

    public RetryableTaskBuilder<T> error(Consumer<RetryableTaskError> onError){
        this.onError = onError;
        return this;
    }


    public RetryableTask<T> build(){

        if(doLogic == null || onDone == null)
            throw new IllegalArgumentException("Invalid retryable task.");

        if(name == null)
            name = "";

        return new RetryableTask<T>(name) {
            @Override
            public T doLogic() throws Exception {
                return doLogic.execute(this);
            }

            @Override
            public void onDone(T object) {
                onDone.accept(new RetryableTaskReturn<T>(this, object));
            }

            @Override
            public <E extends Exception> void onError(E exception) {
                if(onError != null)
                    onError.accept(new RetryableTaskError(this, exception));
            }
        };
    }

    public void buildAndQueue(RetryableTaskExecutor taskExecutor){
        taskExecutor.queue(build());
    }

}
