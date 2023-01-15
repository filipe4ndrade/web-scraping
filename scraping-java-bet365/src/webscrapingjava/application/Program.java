package webscrapingjava.application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Program {

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_YELLOW = "\u001B[33m";

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);

		System.out.println("===PLACAR DE FUTEBOL===");
		System.out.println("\nEscolha uma Opção:\n1- Placar de Hoje\n2- Placar de Ontem");
		System.out.print("Digite 1 ou 2: ");
		int digito = sc.nextInt();
		System.out.println();
		String opcao = "";

		switch (digito) {

		case 1:
			opcao = "jogos-de-hoje";
			break;
		case 2:
			opcao = "jogos-de-ontem";
			break;
		default:
			break;
		}

		List<String> paulista = new ArrayList<>();
		List<String> carioca = new ArrayList<>();
		List<String> saoPaulo = new ArrayList<>();
		List<String> paranaense = new ArrayList<>();
		List<String> baiano = new ArrayList<>();
		List<String> catarinense = new ArrayList<>();
		List<String> goiano = new ArrayList<>();
		List<String> pernambucano = new ArrayList<>();
		List<String> alagoano = new ArrayList<>();

		try {
			Document url = Jsoup.connect("https://www.placardefutebol.com.br/" + opcao).userAgent(
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
					.ignoreHttpErrors(true).followRedirects(true).timeout(100000).ignoreContentType(true).get();

			Elements lista = url.getElementsByClass("row align-items-center content");

			int count = 1;

			for (Element obj : lista) {

				if (!obj.getElementsByClass("badge badge-default").isEmpty()) {
					String campeonato = url.getElementsByTag("a").get(count + 5).attr("href");

					//System.out.println(campeonato);
					if (campeonato.charAt(0) == 'h') {
						count += 2;
						campeonato = url.getElementsByTag("a").get(count + 5).attr("href");
					}
					if (campeonato.indexOf("/", 2) < 0) {
						count++;
						campeonato = url.getElementsByTag("a").get(count + 5).attr("href");
					}

					//System.out.println(campeonato);
					String status = obj.getElementsByClass("badge badge-danger status-name").text();

					if (status.isEmpty()) {
						status = obj.getElementsByClass("badge badge-success status-name blink").text();

						if (status.isEmpty()) {
							status = obj.getElementsByClass("badge badge-warning status-name").text();
						}
					}
					String time1 = obj.getElementsByClass("text-right team_link").text();
					Element gols1 = obj.getElementsByClass("badge badge-default").first();
					String time2 = obj.getElementsByClass("text-left team_link").text();
					Element gols2 = obj.getElementsByClass("badge badge-default").get(1);

					// if (gols1 != null && gols2 != null) {

					if (campeonato.charAt(0) == '/' && campeonato.indexOf("/", 2) > 0) {
						campeonato = campeonato.substring(0, campeonato.indexOf("/", 2));
					}

					String gol1 = gols1.text();
					String gol2 = gols2.text();
					System.out.println(ANSI_YELLOW + campeonato.replaceAll("/", "").replaceAll("-", " ") + ANSI_RESET);
					System.out.println(
							"        " + status + "\n" + time1 + "|" + gol1 + "|" + " X " + "|" + gol2 + "|" + time2);
					System.out.println();

					// }

				}
				count++;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}

	}

}
