package dev.br.brunoxkk0.retryable.test;

import dev.br.brunoxkk0.retryable.builder.RetryableTaskBuilder;
import dev.br.brunoxkk0.retryable.core.RetryableTask;
import dev.br.brunoxkk0.retryable.core.RetryableTaskExecutor;


public class Main {

    public static void main(String[] args) {

        RetryableTaskExecutor retryableTaskExecutor = new RetryableTaskExecutor();

        RetryableTask<String> s = RetryableTaskBuilder.<String>create().named("Teste 1").of((task) -> {
            Thread.sleep(1000);

            if(task.getAttempt() != 3){
                throw new Exception("Deu erro hein" + task.getName());
            }

            return "Thread " + task.getName() + " Passou";
        }).done((taskReturn) -> {
            System.out.println("Retorno do objeto " + taskReturn.task().getName() + ": " + taskReturn.returnValue());
        }).error((e) -> {
            System.out.println("Error: " + e.getLocalizedMessage());
        }).build();

        retryableTaskExecutor.queue(s);

        RetryableTask<String> s2 = RetryableTaskBuilder.<String>create().named("Teste 2").of((task) -> {
            Thread.sleep(1000);

            if(task.getAttempt() != 3){
                throw new Exception("Deu erro hein" + task.getName());
            }

            return "Thread " + task.getName() + " Passou";
        }).done((taskReturn) -> {
            System.out.println("Retorno do objeto " + taskReturn.task().getName() + ": " + taskReturn.returnValue());
        }).error((e) -> {
            System.out.println("Error: " + e.getLocalizedMessage());
        }).build();

        retryableTaskExecutor.queue(s2);


        for(int i = 0; i < 5; i++) {
            retryableTaskExecutor.queue(new RetryableTask<String>(" - " + i) {

                @Override
                public String doLogic() throws Exception {

                    Thread.sleep(1000);

                    if (getAttempt() != 3) {
                        throw new Exception("Deu erro hein" + getName());
                    }

                    return "Thread " + getName() + " Passou";
                }

                @Override
                public void onDone(String object) {
                    System.out.println("Retorno do objeto " + getName() + ": " + object);
                }

                @Override
                public <E extends Exception> void onError(E exception) {
                    System.out.println("Error: " + exception.getLocalizedMessage());
                }

            });

        }
    }
}
