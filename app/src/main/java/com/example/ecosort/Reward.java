package com.example.ecosort;

import java.util.ArrayList;
import java.util.List;

public class Reward {
    public String icon;
    public String title;
    public String description;
    public int cost;

    public Reward(String icon, String title, String description, int cost) {
        this.icon = icon;
        this.title = title;
        this.description = description;
        this.cost = cost;
    }

    public static List<Reward> catalog() {
        List<Reward> list = new ArrayList<>();
        list.add(new Reward("🏅", "Eco Champion badge", "Unlock a badge on your profile", 30));
        list.add(new Reward("☕", "Free coffee coupon", "Redeemable at partner cafés", 80));
        list.add(new Reward("🛍️", "Reusable tote bag", "A sturdy eco-friendly tote bag", 100));
        list.add(new Reward("🌳", "Plant a tree", "We plant a tree in your name", 150));
        list.add(new Reward("🎟️", "Movie ticket voucher", "One free movie ticket", 200));
        list.add(new Reward("💚", "₹50 charity donation", "Donated to an environmental charity", 250));
        return list;
    }
}