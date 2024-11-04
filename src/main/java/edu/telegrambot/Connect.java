package edu.telegrambot;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.HashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@Component
public class Connect extends TelegramLongPollingBot {

    private final TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
    private final int delay = 5;
    private HashMap<Long, ScheduledExecutorService> services = new HashMap<>();

    @Value("${bot.name}")
    private String botName;
    @Getter
    @Value("${bot.token}")
    private String botToken;

    public Connect() throws TelegramApiException {
    }

    @PostConstruct
    public void init() throws TelegramApiException {
        telegramBotsApi.registerBot(this);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            switch(messageText) {
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    {
                        startSending(chatId);
                        return;
                    }
                case "/stop": {
                        services.get(chatId).shutdown();
                        services.remove(chatId);
                        return;
                }
                default: {
                    String badCommand = "Please enter command \"start\"";
                    sendMessage(chatId, badCommand);
                }
            }
            }
        }

    private void startCommandReceived(Long chatId, String name) {
        String answer = "Hi, " + name + ", nice to meet you!" + "\n";
        sendMessage(chatId, answer);}

    private void startSending(Long chat){
        if(services.containsKey(chat)) return;
        ScheduledExecutorService service = new ScheduledThreadPoolExecutor(1);
        RunnableTask task = new RunnableTask(this, chat);
        services.put(chat, service);
        service.scheduleWithFixedDelay(task, 1, delay, TimeUnit.SECONDS);
    }

    protected void sendMessage(Long chatId, String textToSend){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException ignored) {
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }

}