# Инструкция по запуску

## О проекте

Данный проект написан в рамках выполнения тестового задания.

## Условия тестового задания

### Задание

Разработать Java-приложение - мессенджер, на базе Redis (пользовательский интерфейс на выбор, допускается консольный). 
Приложение должно обладать следующим функционалом:
1. создавать пользователя и начинать общение от этого пользователя; 
2. просматривать список зарегистрированных пользователей;
3. отправлять и получать сообщения любому пользователю в базе;
4. хранить и отображать последнюю историю сообщений;
5. реализовать конфигурационный файл, в котором хранятся какие-то параметры (как минимум, параметры подключения к Redis); 6. возможность "онлайн" общения пользователей (через Redis)
7. экспортировать переписку в текстовый файл в формате JSON.
   
### Требования

- Создать Gradle проект в IntelliJ IDEA;
- Обложить проект unit-тестами;
- Разрабатывать и хранить приложение в Git репозитории (например, https://bitbucket.org); 
- НЕ использовать spring framework.
- Инструкция по запуску приложения в readme
   
Как проверяется
   - Попытка запустить приложение по инструкции
   - Проверяется основной функционал: запустить одно приложение, создать одного пользователя. запустить второе приложение, создать пользователя, отправить сообщение первому. У первого получено сообщение, отправить обратно - у второго получено.
   - Проверяется побочный функционал (конфигурирование, экспорт, просмотр переписки)
   - Проверяется чистота, оформление и структура кода и проекта

## Архитектура проекта

###  Основное

Приложение состоит из серверной части, написанной на Java 17, и  клиентской части, написанной на JS.
Серверная часть предоставляет REST API для управления пользователями и получения истории сообщений.
Для обмена сообщениями в реальном времени использованы websocket.

### Disclaimer (ограничения и допущения)

Данное приложение разрабатывалось в рамках тестового задания, а потому имеет следующие ограничения/допущения:

- не реализован защищенный доступ к эндпойнтам
- не реализовано управление websocket-сессиями для мультидеплоя
- несмотря на требование тестового задания покрыть приложение юнит-тестами, в данном приложении приведен всего один
  юнит-тест в качестве примера. Мотивацией для нарушения данного требования является следующее предположение: 
  покрытие юнит-тестами никак не отражает технический уровень кандидата. В реальном продакшн-коде, безусловно,
  должны быть тесты, однако в рамках выполнения тестового задания полнота покрытия смысла не имеет.
    
- поскольку тестовое задание рассчитано на бэкенд-разработчика, клиентский код на js был написан по принципу "работает-и ладно".
Данный клиентский код рассчитан только на демонстрирование работоспособности серверной части. 

## Быстрый запуск

### Требования

1. Наличие установленного docker
2. Наличие запущенного экземпляра Redis

### Запуск серверной части

1. Склонировать репозиторий проекта chat
2. В конфигурационном файле config.yml (находится в папке src/main/resources) задать параметры подключения к Redis
   (если Redis запущен в docker, использовать его IP во избежание проблем с разрешением имен DNS)
3. В терминале зайти в директорию проекта chat
4. Запустить скрипт run.sh (при необходимости выдать ему права на выполнение командой chmod +X run.sh)

При успешном выполнении в логах будет сообщение с примерным содержанием:

```
22-Jun-2023 12:33:21.453 INFO [main] org.apache.catalina.startup.Catalina.start Server startup in [678] milliseconds
```

### Запуск клиентской части

0. Склонировать репозиторий проекта chatclient
1. В терминале зайти в директорию проекта chatclient
2. Запустить скрипт run.sh (при необходимости выдать ему права на выполнение командой chmod +X run.sh)

При успешном выполнении в логах будет сообщение с примерным содержанием:

```
2023/06/22 12:29:56 [notice] 1#1: start worker process 32
```

## Запуск на Windows

### Требования

1. Установленный docker

### Запуск серверной части

1. В терминале зайти в директорию проекта chat
2. Выполнить команды

```
gradlew.bat war
gradlew.bat assemble
docker build . -t chat
docker run -p 8080:8080 chat
```

### Запуск клиентской части

1. В терминале зайти в директорию проекта chatclient
2. Выполнить команды

```
docker build . -t chatclient
docker run -p 8085:80 chatclient
```

## Проверка работоспособности

1. Запустить серверную часть
2. Запустить клиентскую часть
3. Открыть в двух вкладках страницу http://localhost:8085
4. (Опциональный шаг, если пользователи еще не создавались)
   На первой открытой вкладке в поле 'user name' вписать имя создаваемого пользователя
   и нажать кнопку Create.
5. (Опциональный шаг, если пользователи еще не создавались) 
   Для создания второго пользователя повторить п.4, выбрав другое имя.
6. Нажать кнопку Refresh users
7. В выпадающем списке рядом с меткой Whoami выбрать текущего пользователя
8. В выпадающем списке рядом с меткой Send to выбрать пользователя, с которым планируется чат
9. Нажать кнопку Start chat (при успешном старте метка кнопки изменится на Connected)
10. В поле message ввести текст сообщения
11. Нажать кнопку Send (при успешной отправке в текстовой зоне ниже появится отправленное сообщение с именем отправителя)
12. Перейти во вторую вкладку и выбрать в качестве Whoami пользователя, выбранного в предыдущей вкладке
    как Send to, а в качестве Send to - пользователя, выбранного в предыдущей вкладке как Whoami
    (иными словами, на второй вкладке поменять их местами)
13. Нажать кнопку Start chat
14. В поле message второй вкладки ввести текст сообщения и нажать кнопку Send
15. Перейти на первую вкладку (при успешной отправке сообщение, отправленное со второй вкладки, 
    будет видно в текстовой области первой вкладки)
16. Для выгрузки истории чата нажать на кнопку 'Download history'.
