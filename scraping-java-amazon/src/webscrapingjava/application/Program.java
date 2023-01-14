package webscrapingjava.application;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
			System.out.println("=====Escritores Independentes na Amazon=====");
			System.out.println("");
			System.out.println("O programa busca retornar apenas livros de autores independentes e nacionais.");
			System.out.print("Digite o termo para a busca: ");
			String buscar = sc.nextLine();
			buscar = buscar.replaceAll(" ", "+");
			System.out.println();

			String chave = "x";

			int pages = 1;
			do {
				try {

					System.out.println("====" + pages + "° PÁGINA DE BUSCA====\n");

					// userAgent: http://www.useragentstring.com
					Document url = Jsoup
							.connect("https://www.amazon.com.br/s?k=" + buscar + "&i=stripbooks&page=" + pages
									+ "&__mk_pt_BR=%C3%85M%C3%85%C5%BD%C3%95%C3%91&crid=1I4MJBWNVKKB8&sprefix=" + buscar
									+ "%2Cstripbooks%2C199&ref=nb_sb_noss_" + pages)
							.userAgent(
									"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
							.ignoreHttpErrors(true).followRedirects(true).timeout(100000).ignoreContentType(true).get();

					Elements resultList = url.select("#search div.s-result-list div[data-asin]");

					int count = 0;
					for (Element currentProduct : resultList) {
						String asin = currentProduct.attr("data-asin");

						if (asin.isEmpty()) {
							continue;
						}

						if (asin.charAt(0) == 'B'
								&& currentProduct.getElementsByClass("a-size-base").text().indexOf("g") == -1) {
							System.out.println(ANSI_YELLOW + count + " ASIN:" + asin + ANSI_RESET);

							// TITULO DO LIVRO
							String titulo = currentProduct.getElementsByClass("a-size-medium a-color-base a-text-normal")
									.text();
							System.out.println("Livro: " + titulo);

							// AUTOR
							String autor = currentProduct.getElementsByClass("a-size-base").text();
							if (autor.charAt(0) == 'p')
								System.out.println("Autor:"
										+ autor.substring(autor.indexOf("por"), autor.indexOf("|")).replaceAll("por", ""));

							else if (autor.charAt(0) == 'E' || autor.charAt(0) == 'A')
								System.out.println("Autor:" + autor
										.substring(autor.indexOf("por", 2), autor.indexOf("|", 2)).replaceAll("por", ""));

							else
								System.out.println("Autor: " + autor);

							// PRECO
							String preco = currentProduct.getElementsByClass("a-price").first().text();
							System.out.println("Preço: " + preco.substring(0, preco.lastIndexOf("R$")));

							// LINK DA PAGINA NA AMAZON
							String link = url.getElementsByClass(
									"a-link-normal s-underline-text s-underline-link-text s-link-style a-text-normal")
									.get(count).attr("href");
							URI linkLivro = new URI(link);
							String printLink = "https://www.amazon.com.br" + link.substring(0, link.indexOf("ref="));
							System.out.println("Link: " + printLink);

							// DESCRICAO
							Document url2 = Jsoup.connect(printLink).userAgent(
									"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
									.get();
							String descricao = url2
									.getElementsByClass("a-expander-content a-expander-partial-collapse-content").text();
							System.out.println("Descrição: " + descricao);
							
							// LINK DA IMAGEM
							String linkImagem = currentProduct.getElementsByTag("img").attr("src");
							URI urlImagem = new URI(linkImagem);
							System.out.println("URL Imagem: " + linkImagem);
							System.out.println();

						}
						count++;
					}

					System.out.print("Digite enter para ver mais ou x para sair: ");
					chave = sc.nextLine();
					pages++;

				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 

			} while (!chave.equalsIgnoreCase("x"));
			System.out.println("\n=====Programa Encerrado=====");
			sc.close();

	}

}
