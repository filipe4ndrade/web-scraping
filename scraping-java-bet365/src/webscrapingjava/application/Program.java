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
	public static final String ANSI_RED = "\u001B[31m";

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);

		System.out.println(ANSI_YELLOW + "=====PLACAR DE FUTEBOL=====" + ANSI_RESET);

		Set<String> paulista = new HashSet<>();
		Set<String> carioca = new HashSet<>();
		Set<String> copaSp = new HashSet<>();
		Set<String> paranaense = new HashSet<>();
		Set<String> baiano = new HashSet<>();
		Set<String> catarinense = new HashSet<>();
		Set<String> goiano = new HashSet<>();
		Set<String> pernambucano = new HashSet<>();
		Set<String> cearense = new HashSet<>();
		Set<String> espanhol = new HashSet<>();
		Set<String> frances = new HashSet<>();
		Set<String> portugues = new HashSet<>();
		Set<String> ingles = new HashSet<>();
		Set<String> todos = new HashSet<>();

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

						if (!obj.getElementsByClass("badge badge-penalties").isEmpty()) {
							String penaltis1 = obj.getElementsByClass("badge badge-penalties").first().text();
							String penaltis2 = obj.getElementsByClass("badge badge-penalties").get(1).text();
							termo = "        " + status + "\n" + time1 + "|" + gols1 + "|" + " X " + "|" + gols2 + "|"
									+ time2 + "\n" + "Pênaltis:   |" + penaltis1 + "|" + " X " + "|" + penaltis2 + "|";
						}

						String campeonatoFormatado = campeonato.replaceAll("/", "").replaceAll("-", " ");
						todos.add(campeonatoFormatado+"\n"+termo);

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

						// Copa São Paulo
						if (campeonato.indexOf("copa") >= 0 && campeonato.indexOf("da") < 0) {
							copaSp.add(termo);
						}
						// Paranaense
						if (campeonato.indexOf("paranaense") >= 0) {
							paranaense.add(termo);
						}
						// Baiano
						if (campeonato.indexOf("baiano") >= 0) {
							baiano.add(termo);
						}
						// Pernambucano
						if (campeonato.indexOf("pernambucano") >= 0) {
							pernambucano.add(termo);
						}
						// Cearense
						if (campeonato.indexOf("cearense") >= 0) {
							cearense.add(termo);
						}
						// Catarinense
						if (campeonato.indexOf("catarinense") >= 0) {
							catarinense.add(termo);
						}
						// Goiano
						if (campeonato.indexOf("goiano") >= 0) {
							goiano.add(termo);
						}
						// Espanhol
						if (campeonato.indexOf("espanhol") >= 0) {
							espanhol.add(termo);
						}
						// Portugues
						if (campeonato.indexOf("portugues") >= 0) {
							portugues.add(termo);
						}
						// Francês
						if (campeonato.indexOf("frances") >= 0) {
							frances.add(termo);
						}
						// Ingles
						if (campeonato.indexOf("ingles") >= 0) {
							ingles.add(termo);
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
				case 3:
					forEach(copaSp);
					break;
				case 4:
					forEach(paranaense);
					break;
				case 5:
					forEach(baiano);
					break;
				case 6:
					forEach(pernambucano);
					break;
				case 7:
					forEach(cearense);
					break;
				case 8:
					forEach(catarinense);
					break;
				case 9:
					forEach(goiano);
					break;
				case 10:
					forEach(ingles);
					break;
				case 11:
					forEach(frances);
					break;
				case 12:
					forEach(espanhol);
					break;
				case 13:
					forEach(portugues);
					break;
				case 0:
					forEach(todos);
					break;

				default:
					break;

				}
				System.out.print(ANSI_YELLOW + "Clique enter para continuar ou x para sair: " + ANSI_RESET);
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
		System.out.println("3- Copa São Paulo ");
		System.out.println("4- Campeonato Paranaense ");
		System.out.println("5- Campeonato Baiano ");
		System.out.println("6- Campeonato Pernambucano ");
		System.out.println("7- Campeonato Cearense ");
		System.out.println("8- Campeonato Catarinense ");
		System.out.println("9- Campeonato Goiano ");
		System.out.println("10- Campeonato Inglês ");
		System.out.println("11- Campeonato Francês ");
		System.out.println("12- Campeonato Espanhol ");
		System.out.println("13- Campeonato Português ");
		System.out.println("0- Todos ");

		System.out.println();
		System.out.print(ANSI_YELLOW + "Digite sua opção: " + ANSI_RESET);
		int selecao = sc.nextInt();
		System.out.println();

		return selecao;

	}

	public static void forEach(Set<String> set) {
		if (set.size() == 0)
			System.out.println(ANSI_RED + "Não Houve Jogos!\n" + ANSI_RED);

		for (String obj : set) {
			System.out.println(obj);
			System.out.println();
		}

	}

}
