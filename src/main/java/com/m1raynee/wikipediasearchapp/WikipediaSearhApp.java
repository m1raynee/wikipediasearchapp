package com.m1raynee.wikipediasearchapp;

import java.awt.Desktop;
import java.net.URI;
import java.util.List;
import java.util.Scanner;

import com.m1raynee.wikipediasearchapp.models.Page;
import com.m1raynee.wikipediasearchapp.models.SearchResult;
import com.m1raynee.wikipediasearchapp.services.WikiService;

public class WikipediaSearhApp {
    private static final String WIKI_PAGE_URL = "https://ru.wikipedia.org/w/index.php?curid=";
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        WikiService service = new WikiService();

        System.out.println("=== Поиск статьи по Википедии ===");

        while (true) {
            System.out.print("Введите поисковый запрос (или 'exit' для выхода): ");
            String query = scanner.nextLine().trim();

            if (query.equalsIgnoreCase("exit")) {
                System.out.println("Выход из программы...");
                break;
            }
            try {
                SearchResult result = service.search(query);
                List<Page> pages = result.getQuery().getSearch();

                if (pages == null || pages.isEmpty()) {
                    System.out.println("По запросу " + query + " ничего не найдено. Попробуйте другой запрос.");
                    continue;
                }
                System.out.println("\nНайдено " + pages.size() + " статей:");
                for (int i = 0; i < pages.size(); i++) {
                    System.out.printf(" %d. %s\n", i+1, pages.get(i).toString());
                }
                System.out.println("----------------");

                selectAndOpenArticle(scanner, pages);
                System.out.println("Готов к новому запросу.");

            } catch (Exception e) {
                System.err.println("Произошла ошибка в ходе выполнения поиска: " + e.getMessage());
                e.printStackTrace();
            }
        }

        scanner.close();
    }

    private static void selectAndOpenArticle(Scanner scanner, List<Page> pages) {
        while (true) {
            System.out.print("Введите номер статьи для открытия (1-" + pages.size() + ", или 'n' для нового поиска): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("n")) {
                return;
            }

            try {
                int selection = Integer.parseInt(input);
                
                if (selection < 1 || selection > pages.size()) {
                    System.out.println("Некорректный номер. Пожалуйста, введите число от 1 до " + pages.size() + ".");
                    return;
                }
                Page selectedPage = pages.get(selection - 1);
                openArticleInBrowser(selectedPage.getPageid());

            } catch (NumberFormatException e) {
                System.out.println("Некорректный ввод. Пожалуйста, введите номер статьи или 'n'.");
            } catch (Exception e) {
                System.err.println("Не удалось открыть статью в браузере: " + e.getMessage());
                return; 
            }
        }
    }

    private static void openArticleInBrowser(int pageId) throws Exception {
        URI uri = new URI(WIKI_PAGE_URL + pageId);
        System.out.println("\nОткрываю статью в браузере: " + uri);

        if (!Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            throw new UnsupportedOperationException("Функция открытия браузера не поддерживается вашей системой.");
        }

        Desktop.getDesktop().browse(uri);
    }
}