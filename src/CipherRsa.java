import javax.crypto.Cipher;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class CipherRsa extends JFrame
{

    private static final String ALGORITHM = "RSA";
    private static final int KEY_SIZE = 2048;
    private static final String PRIVATE_KEY_FILE = "private.key";
    private static final String PUBLIC_KEY_FILE = "public.key";

    private PrivateKey privateKey;
    private PublicKey publicKey;

    private JTextArea inputArea;
    private JTextArea outputArea;
    private JLabel statusLabel;

    public CipherRsa()
    {
        setTitle("RSA-шифрование");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        createInterface();
    }

    private void createInterface() {
        setLayout(new BorderLayout(10, 10));

        JPanel buttonPanel = new JPanel(new GridLayout(2, 5, 5, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton genKeysBtn = new JButton("Генерировать ключи");
        JButton loadKeysBtn = new JButton("Загрузить ключи");
        JButton saveKeysBtn = new JButton("Сохранить ключи");
        JButton encryptBtn = new JButton("Зашифровать");
        JButton decryptBtn = new JButton("Расшифровать");
        JButton signBtn = new JButton("Подписать");
        JButton verifyBtn = new JButton("Проверить");
        JButton clearBtn = new JButton("Очистить");
        JButton exitBtn = new JButton("Выход");

        genKeysBtn.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) { generateKeys(); }
        });

        loadKeysBtn.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) { loadKeys(); }
        });

        saveKeysBtn.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) { saveKeys(); }
        });

        encryptBtn.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) { encrypt(); }
        });

        decryptBtn.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) { decrypt(); }
        });

        signBtn.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) { sign(); }
        });

        verifyBtn.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) { verify(); }
        });

        clearBtn.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) { clear(); }
        });

        exitBtn.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) { System.exit(0); }
        });

        buttonPanel.add(genKeysBtn);
        buttonPanel.add(loadKeysBtn);
        buttonPanel.add(saveKeysBtn);
        buttonPanel.add(encryptBtn);
        buttonPanel.add(decryptBtn);
        buttonPanel.add(signBtn);
        buttonPanel.add(verifyBtn);
        buttonPanel.add(clearBtn);
        buttonPanel.add(exitBtn);

        inputArea = new JTextArea(10, 40);
        inputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        outputArea = new JTextArea(10, 40);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        outputArea.setEditable(false);

        JPanel textPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        textPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Входное сообщение"));
        inputPanel.add(new JScrollPane(inputArea), BorderLayout.CENTER);

        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.setBorder(BorderFactory.createTitledBorder("Результат"));
        outputPanel.add(new JScrollPane(outputArea), BorderLayout.CENTER);

        textPanel.add(inputPanel);
        textPanel.add(outputPanel);

        statusLabel = new JLabel("Готов к работе");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        add(buttonPanel, BorderLayout.NORTH);
        add(textPanel, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
    }

    private void generateKeys()
    {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORITHM);
            generator.initialize(KEY_SIZE);
            KeyPair pair = generator.generateKeyPair();

            privateKey = pair.getPrivate();
            publicKey = pair.getPublic();

            statusLabel.setText("Ключи сгенерированы.");
            outputArea.setText("Ключи RSA (2048 бит) созданы.\n\n");
            outputArea.append("Открытый ключ готов к использованию.\n");
            outputArea.append("Закрытый ключ сохранен в памяти.");

            JOptionPane.showMessageDialog(this,
                    "Ключи успешно созданы.",
                    "Успех",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            showError("Ошибка генерации.", e);
        }
    }

    private void saveKeys()
    {
        if (privateKey == null || publicKey == null)
        {
            JOptionPane.showMessageDialog(this,
                    "Сначала создайте ключи.",
                    "Внимание!",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Сохраняем ключи в файлы
            Files.write(new File(PRIVATE_KEY_FILE).toPath(), privateKey.getEncoded());
            Files.write(new File(PUBLIC_KEY_FILE).toPath(), publicKey.getEncoded());

            statusLabel.setText("Ключи сохранены.");
            outputArea.setText("Ключи сохранены в файлы:\n");
            outputArea.append("- " + PRIVATE_KEY_FILE + "\n");
            outputArea.append("- " + PUBLIC_KEY_FILE);

        } catch (Exception e) {
            showError("Ошибка сохранения.", e);
        }
    }

    private void loadKeys()
    {
        try {
            File privateFile = new File(PRIVATE_KEY_FILE);
            File publicFile = new File(PUBLIC_KEY_FILE);

            if (!privateFile.exists() || !publicFile.exists()) {
                JOptionPane.showMessageDialog(this,
                        "Файлы ключей не найдены.",
                        "Ошибка!",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            byte[] privateBytes = Files.readAllBytes(privateFile.toPath());
            byte[] publicBytes = Files.readAllBytes(publicFile.toPath());

            KeyFactory factory = KeyFactory.getInstance(ALGORITHM);

            privateKey = factory.generatePrivate(new PKCS8EncodedKeySpec(privateBytes));
            publicKey = factory.generatePublic(new X509EncodedKeySpec(publicBytes));

            statusLabel.setText("Ключи загружены.");
            outputArea.setText("Ключи загружены из файлов:\n");
            outputArea.append("- " + PRIVATE_KEY_FILE + "\n");
            outputArea.append("- " + PUBLIC_KEY_FILE);

        } catch (Exception e) {
            showError("Ошибка загрузки", e);
        }
    }

    private void encrypt()
    {
        if (publicKey == null)
        {
            JOptionPane.showMessageDialog(this,
                    "Сначала загрузите ключи.",
                    "Внимание!",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String text = inputArea.getText().trim();
        if (text.isEmpty())
        {
            JOptionPane.showMessageDialog(this,
                    "Введите текст для шифрования.",
                    "Внимание!",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] encrypted = cipher.doFinal(text.getBytes("UTF-8"));

            String result = Base64.getEncoder().encodeToString(encrypted);
            outputArea.setText("ЗАШИФРОВАННОЕ СООБЩЕНИЕ (Base64):\n\n");
            outputArea.append(result);

            Files.write(new File("encrypted.dat").toPath(), encrypted);
            outputArea.append("\n\nСохранено в файл: encrypted.dat");

            statusLabel.setText("Сообщение зашифровано.");

        } catch (Exception e) {
            showError("Ошибка шифрования!", e);
        }
    }

    private void decrypt()
    {
        if (privateKey == null) {
            JOptionPane.showMessageDialog(this,
                    "Сначала загрузите ключи.",
                    "Внимание!",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            File encFile = new File("encrypted.dat");
            if (!encFile.exists()) {
                JOptionPane.showMessageDialog(this,
                        "Нет зашифрованного файла (encrypted.dat).",
                        "Ошибка!",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            byte[] encrypted = Files.readAllBytes(encFile.toPath());
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] decrypted = cipher.doFinal(encrypted);
            String result = new String(decrypted, "UTF-8");

            outputArea.setText("РАСШИФРОВАННОЕ СООБЩЕНИЕ:\n\n");
            outputArea.append(result);

            Files.write(new File("decrypted.txt").toPath(), result.getBytes());
            outputArea.append("\n\nСохранено в файл: decrypted.txt");

            statusLabel.setText("Сообщение расшифровано.");

        } catch (Exception e) {
            showError("Ошибка расшифровки.", e);
        }
    }

    private void sign()
    {
        if (privateKey == null)
        {
            JOptionPane.showMessageDialog(this,
                    "Сначала загрузите ключи.",
                    "Внимание!",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String text = inputArea.getText().trim();
        if (text.isEmpty())
        {
            JOptionPane.showMessageDialog(this,
                    "Введите текст для подписания.",
                    "Внимание!",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Signature sign = Signature.getInstance("SHA256withRSA");
            sign.initSign(privateKey);
            sign.update(text.getBytes("UTF-8"));

            byte[] signature = sign.sign();

            Files.write(new File("signature.sig").toPath(), signature);

            outputArea.setText("ЦИФРОВАЯ ПОДПИСЬ (Base64):\n\n");
            outputArea.append(Base64.getEncoder().encodeToString(signature));
            outputArea.append("\n\nСохранено в файл: signature.sig");
            outputArea.append("\n\nПодписано сообщение: \"" + text + "\"");
            statusLabel.setText("Подпись создана.");

        } catch (Exception e) {
            showError("Ошибка создания подписи!", e);
        }
    }

    private void verify()
    {
        if (publicKey == null)
        {
            JOptionPane.showMessageDialog(this,
                    "Сначала загрузите ключи.",
                    "Внимание!",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String text = inputArea.getText().trim();
        if (text.isEmpty())
        {
            JOptionPane.showMessageDialog(this,
                    "Введите текст для проверки подписи.",
                    "Внимание!",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            File sigFile = new File("signature.sig");
            if (!sigFile.exists())
            {
                JOptionPane.showMessageDialog(this,
                        "Нет файла с подписью (signature.sig).",
                        "Ошибка!",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            byte[] signature = Files.readAllBytes(sigFile.toPath());

            Signature verify = Signature.getInstance("SHA256withRSA");
            verify.initVerify(publicKey);
            verify.update(text.getBytes("UTF-8"));

            boolean isValid = verify.verify(signature);

            outputArea.setText("ПРОВЕРКА ПОДПИСИ\n\n");
            outputArea.append("Сообщение: \"" + text + "\"\n\n");

            if (isValid) {
                outputArea.append("ПОДПИСЬ ДЕЙСТВИТЕЛЬНА!\n");
                outputArea.append("Личность отправителя ПОДТВЕРЖДЕНА");
                statusLabel.setText("Подпись верна.");
            } else {
                outputArea.append("ПОДПИСЬ НЕДЕЙСТВИТЕЛЬНА!\n");
                outputArea.append("Сообщение было изменено или отправитель не тот.");
                statusLabel.setText("Подпись неверна");
            }

        } catch (Exception e) {
            showError("Ошибка проверки", e);
        }
    }

    private void clear()
    {
        inputArea.setText("");
        outputArea.setText("");
        statusLabel.setText("Поля очищены.");
    }

    private void showError(String message, Exception e)
    {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this,
                message + ": " + e.getMessage(),
                "Ошибка",
                JOptionPane.ERROR_MESSAGE);
        statusLabel.setText(" " + message);
    }

    public static void main(String[] args)
    {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                new CipherRsa().setVisible(true);
            }
        });
    }
}