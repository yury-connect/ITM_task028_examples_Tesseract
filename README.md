

---
## Запустите PostgreSQL в Docker
```bash
docker run --name my-postgres -e POSTGRES_USER=user -e POSTGRES_PASSWORD=1234 -e POSTGRES_DB=demo_db -p 5432:5432 -d postgres:16
```

### Что делает эта команда:
- `--name my-postgres` — имя контейнера `my-postgres`
- `-e POSTGRES_USER=user` — создаёт пользователя `user`
- `-e POSTGRES_PASSWORD=1234` — задаёт пароль `1234`
- `-e POSTGRES_DB=demo_db` — создаёт базу данных `demo_db`
- `-p 5432:5432` — пробрасывает порт 5432 из контейнера на хост
- `-d` — запуск в фоновом режиме
- `postgres:16` — образ PostgreSQL версии 16 (можно указать другую версию)

---
## Проверьте, что контейнер запущен
```bash
docker ps
```
Должны увидеть контейнер `my-postgres` в статусе `Up`.

---
## Проверьте подключение к БД
Используйте `psql` или любой клиент (например, DBeaver, pgAdmin):
```bash
docker exec -it my-postgres psql -U user -d demo_db
```
Или подключитесь из вашего приложения с настройками из `application.yml`

---
## Дополнительные команды Docker
Остановить контейнер: `docker stop my-postgres`
Запустить снова: `docker start my-postgres`
Удалить контейнер: `docker rm my-postgres` _(после остановки)_

---
# Скачиваем и настраиваем языковые данные для Tesseract

Скачайте нужные языки (например, eng, rus) с [официального репозитория](https://github.com/tesseract-ocr/tessdata):

Вот прямые ссылки для скачивания:
- Русский язык: [rus.traineddata](https://github.com/tesseract-ocr/tessdata/raw/main/rus.traineddata)
- Английский язык: [eng.traineddata](https://github.com/tesseract-ocr/tessdata/raw/main/eng.traineddata)
положить в папку `src/main/resources/tessdata/`

Для Linux/Mac: используйте команды в терминале:
```bash
mkdir -p src/main/resources/tessdata
cd src/main/resources/tessdata
curl -L -o eng.traineddata https://github.com/tesseract-ocr/tessdata/raw/main/eng.traineddata
curl -L -o rus.traineddata https://github.com/tesseract-ocr/tessdata/raw/main/rus.traineddata
```

```text
your-project/
└── src/
    └── main/
        └── resources/
            └── tessdata/
                ├── eng.traineddata
                └── rus.traineddata
```