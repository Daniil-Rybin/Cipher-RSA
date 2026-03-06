# RSA шифрование / RSA Encryption

![Java](https://img.shields.io/badge/Java-17%2B-orange?style=for-the-badge&logo=java)
![Swing](https://img.shields.io/badge/GUI-Swing-blue?style=for-the-badge)
![RSA](https://img.shields.io/badge/Algorithm-RSA--2048-green?style=for-the-badge)

## О проекте / About

**Русский**  
Десктопное приложение с графическим интерфейсом для шифрования и дешифрования текста с помощью асимметричного алгоритма RSA. Программа реализует полный цикл работы с криптосистемой: генерацию ключей, шифрование/расшифровку сообщений, создание и проверку цифровой подписи для подтверждения личности отправителя. Разработана на чистой Java с использованием библиотеки Swing.

**English**  
Desktop application with a graphical interface for encrypting and decrypting text using the RSA asymmetric algorithm. The program implements the full cycle of working with a cryptosystem: key generation, message encryption/decryption, creation and verification of digital signatures to confirm the sender's identity. Developed in pure Java using the Swing library.

## Возможности / Features

### Основные / Core
| Функция | Описание |
|---------|----------|
| **Генерация ключей** | Создание пары RSA-ключей (2048 бит) |
| **Сохранение ключей** | Экспорт ключей в файлы private.key / public.key |
| **Загрузка ключей** | Импорт существующих ключей из файлов |
| **Шифрование** | Преобразование текста с помощью открытого ключа |
| **Расшифровка** | Восстановление исходного текста закрытым ключом |
| **Подпись** | Создание цифровой подписи закрытым ключом |
| **Проверка** | Верификация подписи открытым ключом |

### Криптографические характеристики / Cryptographic specifications
<details>
<summary>Подробнее / More details</summary>

| Параметр | Значение |
|----------|----------|
| **Алгоритм** | RSA (Rivest–Shamir–Adleman) |
| **Размер ключа** | 2048 бит |
| **Режим шифрования** | ECB |
| **Padding** | PKCS1Padding |
| **Алгоритм подписи** | SHA256withRSA |
| **Формат ключей** | PKCS#8 (приватный) / X.509 (публичный) |

</details>

### Форматы данных / Data formats
| Тип данных | Формат |
|------------|--------|
| Зашифрованные данные | Base64 (отображение) + бинарный (файл) |
| Подпись | Base64 (отображение) + бинарный (файл) |
| Ключи | Бинарный формат (DER-encoded) |

### Интерфейс / Interface
- Две текстовые области (ввод/вывод)
- Панель управления ключами
- Строка статуса с информацией о состоянии ключей
- Интуитивно понятные кнопки

## Быстрый старт / Quick Start

### Требования / Requirements
- Java Runtime Environment (JRE) 17 или выше
- Любая ОС: Windows / Linux / macOS

### Установка и запуск / Installation & Launch

```bash
# Клонировать репозиторий / Clone repository
git clone https://github.com/Daniil-Rybin/Cipher-RSA.git

# Перейти в директорию / Navigate to directory
cd Cipher-RSA

# Скомпилировать / Compile
javac -encoding UTF-8 CipherRsa.java

# Запустить / Run
java CipherRsa
```

## Инструкция по использованию / Usage Guide

### 🇷🇺 Русский

#### Работа с ключами
1. **Генерация ключей** - нажмите "Генерировать ключи" для создания новой пары
2. **Сохранение ключей** - нажмите "Сохранить ключи" для экспорта в файлы
3. **Загрузка ключей** - при повторном запуске нажмите "Загрузить ключи"

#### Шифрование и расшифровка
1. Введите текст в левое поле
2. Убедитесь, что ключи загружены (статус внизу)
3. Нажмите "Зашифровать" или "Расшифровать"
4. Результат появится в правом поле

#### Цифровая подпись
1. Введите сообщение в левое поле
2. Нажмите "Подписать" для создания подписи
3. Для проверки введите сообщение и нажмите "Проверить"

### 🇬🇧 English

#### Key Management
1. **Generate keys** - click "Generate keys" to create a new pair
2. **Save keys** - click "Save keys" to export to files
3. **Load keys** - on restart, click "Load keys" to import

#### Encryption and Decryption
1. Enter text in the left field
2. Ensure keys are loaded (check status)
3. Click "Encrypt" or "Decrypt"
4. Result appears in the right field

#### Digital Signature
1. Enter message in the left field
2. Click "Sign" to create a signature
3. To verify, enter the message and click "Verify"

## Примеры работы / Examples

### Пример 1: Шифрование и расшифровка / Encryption and Decryption
```
Входное сообщение: Привет, мир!
Действие: Зашифровать → Сохранить → Загрузить ключи → Расшифровать
Результат: Привет, мир! ✓
```

### Пример 2: Цифровая подпись / Digital Signature
```
Сообщение: Важный документ
Подпись: (Base64)...

Попытка подделки: Важный документ (изменено)
Проверка: ПОДПИСЬ НЕДЕЙСТВИТЕЛЬНА!
```

### Пример 3: Проверка личности / Identity Verification
```
Отправитель: "Перевод 1000 рублей" + подпись
Получатель: Проверка подписи
Результат: Личность подтверждена
```

## Архитектура / Architecture

```
CipherRsa (JFrame)
├── Поля класса
│   ├── privateKey - закрытый ключ
│   ├── publicKey - открытый ключ
│   └── statusLabel - строка статуса
├── GUI компоненты
│   ├── JTextArea (ввод/вывод)
│   ├── JButton (9 кнопок управления)
│   └── JLabel (статус)
├── Криптографические методы
│   ├── generateKeys() - генерация ключей
│   ├── encrypt() - шифрование
│   ├── decrypt() - расшифровка
│   ├── sign() - создание подписи
│   └── verify() - проверка подписи
└── Работа с файлами
    ├── saveKeys() - сохранение ключей
    ├── loadKeys() - загрузка ключей
    └── автоматическое сохранение данных
```

## Алгоритм работы / Algorithm

### Асимметричное шифрование / Asymmetric Encryption
```
Шифрование:   cipher.init(ENCRYPT_MODE, publicKey)
              encrypted = cipher.doFinal(text)

Расшифровка:  cipher.init(DECRYPT_MODE, privateKey)
              decrypted = cipher.doFinal(encrypted)
```

### Цифровая подпись / Digital Signature
```
Создание:     sign.initSign(privateKey)
              sign.update(text)
              signature = sign.sign()

Проверка:     verify.initVerify(publicKey)
              verify.update(text)
              isValid = verify.verify(signature)
```

## Файловая система / File System

| Файл | Назначение                          |
|------|-------------------------------------|
| `private.key` | Закрытый ключ (только ваш)          |
| `public.key` | Открытый ключ (можно распространять) |
| `encrypted.dat` | Зашифрованные данные                |
| `decrypted.txt` | Расшифрованные данные               |
| `signature.sig` | Цифровая подпись                    |

## Обработка ошибок / Error Handling

| Ситуация | Действие |
|----------|----------|
| Ключи не загружены | Предупреждение пользователю |
| Пустой текст | Сообщение о необходимости ввода |
| Файлы ключей не найдены | Ошибка загрузки |
| Файл encrypted.dat отсутствует | Ошибка расшифровки |
| Файл signature.sig отсутствует | Ошибка проверки |
| Ошибка шифрования/расшифровки | Детальное сообщение |

## Технологии / Technologies

- **Java 17+** - основной язык программирования
- **Swing** - графический интерфейс
- **JCA (Java Cryptography Architecture)** - криптографические операции
- **Base64** - кодирование для отображения

## Структура репозитория / Repository Structure

```
Cipher-RSA/
├── src/
│   └── CipherRsa.java      # Исходный код
└── README.md                # Документация
```

### Важно! / Important!
Следующие файлы генерируются при работе программы и **НЕ ДОЛЖНЫ** попадать в git:
```
private.key    # Секретный ключ
public.key     # Открытый ключ
encrypted.dat  # Зашифрованные данные
decrypted.txt  # Расшифрованные данные
signature.sig  # Цифровая подпись
```

## Вклад в проект / Contributing

1. Форкните репозиторий / Fork the repository
2. Создайте ветку / Create a branch (`git checkout -b feature/improvement`)
3. Зафиксируйте изменения / Commit changes (`git commit -m 'Add improvement'`)
4. Отправьте изменения / Push to branch (`git push origin feature/improvement`)
5. Откройте Pull Request / Open a Pull Request

## Контакты / Contact

- **Автор** - Daniil Rybin
- **Email** - danya.danya.rus31@gmail.com
- **GitHub** - [@Daniil-Rybin](https://github.com/Daniil-Rybin)

## Поддержка проекта / Support

Если вам понравился проект, поставьте звездочку на GitHub! Это очень мотивирует :)

If you like this project, please give it a star on GitHub! It really motivates :)

---
```