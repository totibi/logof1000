package client.cms.view

import client.Ajaxer
import client.cms.view.page.PageView
import autowire._
import org.scalajs.dom
import org.scalajs.dom.html.{LI, UList}
import org.scalajs.dom.raw.HTMLElement
import scalatags.JsDom.short._
import shared.MainAPI
import shared.cms.page.Page


object MainView{

	// main body for html
	def refreshMainView: HTMLElement = {
		menuContainer.updatePagesContainer
		mainContainer
	}

	// block with content of the page
	val contentContainer: HTMLElement = {
		val contentContainer: HTMLElement = div(*.`class` := "contentContainer").render
		contentContainer
	}

	// contentContainer - page content container
	class MenuContainer(contentContainer: HTMLElement) {
		val container: HTMLElement = div(*.`class` := "menuContainer").render
		//		val pagesListContainer: HTMLElement = div().render
		val pagesList: UList = ul.render
		val pageTitleInput = input.render
		val addPageButton =
			button(
				"add page",
				*.onclick := { (event: dom.Event) ⇒
					Ajaxer[MainAPI].addPage(Page(pageTitleInput.value, Nil)).call().foreach {
						createdPage ⇒ pagesList.appendChild(getPagesListElement(createdPage))
					}
				}
			).render
		container.appendChild(pageTitleInput)
		container.appendChild(br.render)
		container.appendChild(addPageButton)
		container.appendChild(pagesList)

		// render list of pages
		def updatePagesContainer: Unit = {
			pagesList.clearFromChilds()
			Ajaxer[MainAPI].getPages().call().foreach {
				pages ⇒ fillPagesListContainer(pages)
			}
		}

		private def fillPagesListContainer(pages: Seq[Page]): Unit = pages.foreach(page ⇒ pagesList.appendChild(getPagesListElement(page)))

		private def getPagesListElement(page: Page): LI = {
			li(
				*.id := s"${page.title}-menu",
				button(
					page.title,
					*.onclick := { (event: dom.Event) ⇒ {
						contentContainer.clearFromChilds()
						contentContainer.appendChild(PageView.page2Element(page))
					}
					}
				),
				button(
					"-",
					*.onclick := { (event: dom.Event) ⇒ {
						contentContainer.clearFromChilds()
						Ajaxer[MainAPI].deletePage(page).call().foreach {
							deletedPage ⇒ pagesList.removeNodesFromElement(_.id == s"${deletedPage.title}-menu")
						}
					}
					}
				)
				//						,
				//						button(
				//							"Kanban",
				//							*.onclick := { (event: dom.Event) ⇒ {
				//								clearComponent(messagesContainer)
				//								PageClient.pageKanban(messagesContainer, page)
				//							}
				//							}
				//						)
			).render
		}
	}

	// block with menu of the page
	val menuContainer: MenuContainer = new MenuContainer(contentContainer)

	// main body for html
	val mainContainer: HTMLElement = {
		val mainContainer: HTMLElement = div(*.`class` := "mainContainer").render
		mainContainer.appendChild(contentContainer)
		mainContainer.appendChild(menuContainer.container)
		mainContainer
	}

}
