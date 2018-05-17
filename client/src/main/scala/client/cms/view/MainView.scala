package client.cms.view

import autowire._
import client.Ajaxer
import client.cms.view.page.PageView
import org.scalajs.dom
import org.scalajs.dom.html.{LI, UList}
import org.scalajs.dom.raw.HTMLElement
import scalatags.JsDom.short._
import shared.MainAPI
import shared.cms.page.Page
import shared.cms.page.kanban.{Kanban, KanbanColumn}


object MainView {

	def openMainView: HTMLElement = {
		menuContainer.updatePagesContainer
		getMainContainer
	}

	// main body for html
	def getMainContainer: HTMLElement = {
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
		val pagesList: UList = ul.render
		val pageTitleInput = input.render
		val addPageButton =
			button(
				"add page",
				*.onclick := { (event: dom.Event) ⇒
					val newKanban = Kanban(Seq(KanbanColumn("ToDo"), KanbanColumn("In Progress"), KanbanColumn("Closed")))
					Ajaxer[MainAPI].addPage(Page(pageTitleInput.value, Nil, newKanban)).call().foreach {
						createdPage ⇒ pagesList.appendChild(getPagesListElement(createdPage))
					}
				}
			).render
		container.appendChildren(
			pageTitleInput,
			br.render,
			addPageButton,
			pagesList
		)

		// render list of pages
		def updatePagesContainer: Unit = {
			// TODO mb better clear after executed future
			pagesList.clearFromChildren()
			Ajaxer[MainAPI].getPages().call().foreach {
				pages ⇒ fillPagesListContainer(pages)
			}
		}

		private def fillPagesListContainer(pages: Seq[Page]): Unit = pages.foreach(page ⇒ pagesList.appendChild(getPagesListElement(page)))

		private def getPagesListElement(page: Page): LI = {
			li(
				*.id := PageView.getPageMenuItemId(page),
				button(
					page.title,
					*.`class` := PageView.getPageMenuBtnClass(page),
					*.onclick := { (event: dom.Event) ⇒ {
						contentContainer.clearFromChildren().appendPage(page)
					}
					}
				),
				button(
					"Kanban",
					*.onclick := { (event: dom.Event) ⇒ {
						contentContainer.clearFromChildren()
						PageView.pageKanban(contentContainer, page)
					}
					}
				),
				button(
					"delete",
					*.onclick := { (event: dom.Event) ⇒ {
						contentContainer.clearFromChildren()
						Ajaxer[MainAPI].deletePage(page).call().foreach {
							deletedPage ⇒ pagesList.removeNodesFromElement(_.id == PageView.getPageMenuItemId(page))
						}
					}
					}
				)
			).render
		}
	}

	// block with menu of the page
	val menuContainer: MenuContainer = new MenuContainer(contentContainer)

	// main body for html
	val mainContainer: HTMLElement = {
		val mainContainer: HTMLElement = div(*.`class` := "mainContainer").render
		mainContainer.appendChildren(
			contentContainer,
			menuContainer.container
		)
	}

}
