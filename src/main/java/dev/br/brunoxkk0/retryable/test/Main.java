package dev.br.brunoxkk0.retryable.test;

import dev.br.brunoxkk0.retryable.builder.RetryableTaskBuilder;
import dev.br.brunoxkk0.retryable.core.RetryableTask;
import dev.br.brunoxkk0.retryable.core.RetryableTaskExecutor;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


public class Main {

    static ThreadPoolExecutor poolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(30, r -> {
        Thread thread = new Thread(r);
        thread.setDaemon(false);
        thread.setName("RetryableTask");
        return thread;
    });

    public static void main(String[] args) {

        RetryableTaskExecutor retryableTaskExecutor = new RetryableTaskExecutor(50, poolExecutor);

        RetryableTask<String> s = RetryableTaskBuilder.<String>create().named("Teste 1").of((task) -> {

            Thread.sleep(1000);

            if(task.getAttempt() != 49){
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

            if(task.getAttempt() != 60){
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

                    Thread.sleep(1500);

                    if (getAttempt() != 49) {
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
