package com.leisan.rangiffler.page;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class BasePage<T extends BasePage<?>> {
    public static final MainPage mainPage = new MainPage();
    public static final MyTravelsPage myTravelsPage = new MyTravelsPage();
    public static final MyProfilePage myProfilePage = new MyProfilePage();
    public static final LoginPage loginPage = new LoginPage();
    public static final PeoplePage peoplePage = new PeoplePage();
}
