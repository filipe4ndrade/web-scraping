package webscrapingjava.application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Program {

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_YELLOW = "\u001B[33m";

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		
		System.out.println(ANSI_YELLOW + "=====PLACAR DE FUTEBOL=====" + ANSI_RESET);

		Set<String> paulista = new HashSet<>();
		Set<String> carioca = new HashSet<>();
		List<String> saoPaulo = new ArrayList<>();
		List<String> paranaense = new ArrayList<>();
		List<String> baiano = new ArrayList<>();
		List<String> catarinense = new ArrayList<>();
		List<String> goiano = new ArrayList<>();
		List<String> pernambucano = new ArrayList<>();
		List<String> alagoano = new ArrayList<>();

		String continuar = "";
		do {

			String busca = menuInicialBusca();
			try {
				Document url = Jsoup.connect("https://www.placardefutebol.com.br/" + busca).userAgent(
						"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
						.ignoreHttpErrors(true).followRedirects(true).timeout(100000).ignoreContentType(true).get();

				Elements lista = url.getElementsByClass("row align-items-center content");

				int count = 1;

				for (Element obj : lista) {

					if (!obj.getElementsByClass("badge badge-default").isEmpty()) {
						String campeonato = url.getElementsByTag("a").get(count + 5).attr("href");

						// System.out.println(campeonato);
						if (campeonato.charAt(0) == 'h') {
							count += 2;
							campeonato = url.getElementsByTag("a").get(count + 5).attr("href");
						}
						if (campeonato.indexOf("/", 2) < 0) {
							count++;
							campeonato = url.getElementsByTag("a").get(count + 5).attr("href");
						}

						// System.out.println(campeonato);
						String status = obj.getElementsByClass("badge badge-danger status-name").text();

						if (status.isEmpty()) {
							status = obj.getElementsByClass("badge badge-success status-name blink").text();

							if (status.isEmpty()) {
								status = obj.getElementsByClass("badge badge-warning status-name").text();
							}
						}
						String time1 = obj.getElementsByClass("text-right team_link").text();
						String gols1 = obj.getElementsByClass("badge badge-default").first().text();
						String time2 = obj.getElementsByClass("text-left team_link").text();
						String gols2 = obj.getElementsByClass("badge badge-default").get(1).text();

						if (campeonato.charAt(0) == '/' && campeonato.indexOf("/", 2) > 0) {
							campeonato = campeonato.substring(0, campeonato.indexOf("/", 2));
						}

						String termo = "        " + status + "\n" + time1 + "|" + gols1 + "|" + " X " + "|" + gols2
								+ "|" + time2;
						/*
						 * System.out.println( ANSI_YELLOW + campeonato.replaceAll("/",
						 * "").replaceAll("-", " ") + ANSI_RESET); String termo = "        " + status +
						 * "\n" + time1 + "|" + gols1 + "|" + " X " + "|" + gols2 + "|" + time2;
						 * System.out.println(termo); System.out.println();
						 */

						// Condicoes para a Lista
						// Paulista
						if (campeonato.indexOf("paulista") >= 0 && campeonato.indexOf("a2") < 0) {
							paulista.add(termo);
						}
						// Carioca
						if (campeonato.indexOf("carioca") >= 0) {
							carioca.add(termo);
						}

					}
					count++;
				}

				System.out.println();

				switch (menuCampeonatos()) {
				case 1:
					forEach(paulista);
					break;
				case 2:
					forEach(carioca);
					break;
				default:
					break;

				}
				System.out.print(ANSI_YELLOW +"Clique enter para continuar ou x para sair: "+ANSI_RESET);
				continuar = sc.nextLine();
				System.out.println();

			} catch (IOException e) {

				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
			}

		} while (!continuar.equalsIgnoreCase("x"));

		System.out.println(ANSI_YELLOW + "=====FIM DA APLICAÇÃO=====" + ANSI_RESET);
		sc.close();
	}

	public static String menuInicialBusca() {
		Scanner sc = new Scanner(System.in);

		System.out.println(ANSI_YELLOW + "\nEscolha uma Opção:" + ANSI_RESET);
		System.out.println("1- Placar de Hoje\n2- Placar de Ontem\n");
		System.out.print(ANSI_YELLOW + "Digite 1 ou 2: " + ANSI_RESET);
		int digito = sc.nextInt();
	

		switch (digito) {

		case 1:
			return "jogos-de-hoje";
		case 2:
			return "jogos-de-ontem";
		default:
			return "";
		}
	}

	public static int menuCampeonatos() {
		Scanner sc = new Scanner(System.in);

		System.out.println(ANSI_YELLOW + "Escolha um campeonato:" + ANSI_RESET);
		System.out.println("1- Campeonato Paulista ");
		System.out.println("2- Campeonato Carioca ");
		
		System.out.println();
		System.out.print(ANSI_YELLOW + "Digite sua opção: " + ANSI_RESET);
		int selecao = sc.nextInt();
		System.out.println();

		return selecao;

	}

	public static void forEach(Set<String> set) {
		if(set.size()==0)
		
		for (String obj : set) {
			System.out.println(obj);
			System.out.println();
		}
		
	}

}
