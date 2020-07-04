package com.doubleknd26.macro.service.mask;

import com.doubleknd26.macro.MacroConfig;
import com.doubleknd26.macro.service.MacroService;
import com.doubleknd26.macro.util.MessageService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CoupangMaskMacroService extends MacroService {

	public CoupangMaskMacroService(MacroConfig.ServiceConfig config) {
		super(config);
	}

	@Override
	protected String getName() {
		return this.getClass().getSimpleName();
	}

	@Override
	protected void login() {
		driver.get(config.getLoginPageUrl());
		driver.sendKeyToElement(By.className("_loginIdInput"), config.getId());
		driver.sendKeyToElement(By.className("_loginPasswordInput"), config.getPw());
		WebElement submitBtn = driver.findClickableElement(By.className("login__button--submit"));
		driver.clickAndWait(submitBtn);
	}

	@Override
	protected void run() {
		while (true) {
			visitWishListPage();
			addInStockItemToCart();
		}
	}

	private void visitWishListPage() {
		try {
			By nextPage = By.className("next-page");
			WebElement element = driver.findClickableElement(nextPage);
			element.click();
		} catch (RuntimeException e) {
			// visit here when the first time to access to wish list page
			// or there is no clickable element which is named by next-page.
			logger.info("Try No. " + ++tryCount);
			driver.get(config.getMacroPageUrl());
		} finally {
			driver.wait(2);
		}
	}

	private void addInStockItemToCart() {
		List<WebElement> wishList = driver.findElements(By.className("wish-item"));
		for (WebElement item : wishList) {
			String name = driver.findElement(item, By.className("item-name")).getText();
			By addToCartBtn = By.className("add-to-cart__btn");
			boolean isExists = driver.isWebElementExists(item, addToCartBtn);
			if (isExists) {
				driver.findClickableElement(addToCartBtn).click();
				String message = name + " 상품이 장바구니에 추가됐습니다.";
				MessageService.getInstance().noti(message, "channel");
			}
		}
	}
}
