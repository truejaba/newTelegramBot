package edu.telegrambot;

public class RunnableTask implements Runnable{

    private Connect connect;
    private Long chatID;
    private String message;

    public RunnableTask(Connect connect, Long chatID) {
        this.connect = connect;
        this.chatID = chatID;
    }

    @Override
    public void run() {
        SearhXolairService service = new SearhXolairService();
        message = service.getActualAdressAndPrice();
        connect.sendMessage(chatID, message);
    }
}
