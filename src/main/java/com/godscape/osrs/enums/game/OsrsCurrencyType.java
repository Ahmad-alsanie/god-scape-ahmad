package com.godscape.osrs.enums.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public enum OsrsCurrencyType {
    // Tradeable Currencies
    GOLD_COINS(() -> "Gold Coins", () -> 1.0),
    PLATINUM_TOKENS(() -> "Platinum Tokens", () -> 1000.0),
    NUMULITE(() -> "Numulite", () -> 500.0),
    TRADING_STICKS(() -> "Trading Sticks", () -> 200.0),
    SHANTAY_PASSES(() -> "Shantay Passes", () -> 150.0),
    SHIP_TICKETS(() -> "Ship Tickets", () -> 300.0),
    MOLE_CLAWS(() -> "Mole Claws", () -> 250.0),
    MOLE_SKINS(() -> "Mole Skins", () -> 400.0),

    // Untradeable Currencies
    ABYSSAL_PEARLS(() -> "Abyssal Pearls", () -> 100.0),
    ANIMA_INFUSED_BARK(() -> "Anima Infused Bark", () -> 120.0),
    AGILITY_ARENA_TICKETS(() -> "Agility Arena Tickets", () -> 80.0),
    BARRONITE_SHARDS(() -> "Barronite Shards", () -> 90.0),
    BLOOD_MONEY(() -> "Blood Money", () -> 110.0),
    BRIMHAVEN_VOUCHERS(() -> "Brimhaven Vouchers", () -> 130.0),
    CASTLE_WARS_TICKETS(() -> "Castle Wars Tickets", () -> 140.0),
    ECTO_TOKENS(() -> "Ecto Tokens", () -> 70.0),
    FROG_TOKENS(() -> "Frog Tokens", () -> 60.0),
    GOLDEN_NUGGETS(() -> "Golden Nuggets", () -> 95.0),
    GLISTENING_TEARS(() -> "Glistening Tears", () -> 85.0),
    HALLOWED_MARKS(() -> "Hallowed Marks", () -> 105.0),
    INTELLIGENCE(() -> "Intelligence", () -> 115.0),
    MARKS_OF_GRACE(() -> "Marks of Grace", () -> 125.0),
    MERMAIDS_TEARS(() -> "Mermaids Tears", () -> 135.0),
    MOLCH_PEARLS(() -> "Molch Pearls", () -> 145.0),
    PARAMAYA_TICKETS(() -> "Paramaya Tickets", () -> 155.0),
    RARE_CREATURE_PARTS(() -> "Rare Creature Parts", () -> 165.0),
    REWARD_TOKEN_CREDITS(() -> "Reward Token Credits", () -> 175.0),
    STARDUST(() -> "Stardust", () -> 185.0),
    TOKKUL(() -> "Tokkul", () -> 195.0),
    TERMITES(() -> "Termites", () -> 205.0),
    UNIDENTIFIED_MINERALS(() -> "Unidentified Minerals", () -> 215.0),
    WARRIOR_GUILD_TOKENS(() -> "Warrior Guild Tokens", () -> 225.0),

    // Virtual Currencies
    BOUNTY_HUNTER_POINTS(() -> "Bounty Hunter Points", () -> 50.0),
    CARPENTER_POINTS(() -> "Carpenter Points", () -> 55.0),
    FOUNDRY_REPUTATION(() -> "Foundry Reputation", () -> 60.0),
    HONOUR_POINTS(() -> "Honour Points", () -> 65.0),
    LAST_MAN_STANDING_POINTS(() -> "Last Man Standing Points", () -> 70.0),
    LEAGUE_POINTS(() -> "League Points", () -> 75.0),
    NIGHTMARE_ZONE_POINTS(() -> "Nightmare Zone Points", () -> 80.0),
    PIECES_OF_EIGHT(() -> "Pieces of Eight", () -> 85.0),
    PIZAZZ_POINTS(() -> "Pizazz Points", () -> 90.0),
    PVP_ARENA_REWARD_POINTS(() -> "PvP Arena Reward Points", () -> 95.0),
    RECLAIM_TOKENS(() -> "Reclaim Tokens", () -> 100.0),
    SLAYER_REWARD_POINTS(() -> "Slayer Reward Points", () -> 105.0),
    SPEEDRUN_POINTS(() -> "Speedrun Points", () -> 110.0),
    THEATRE_OF_BLOOD_POINTS(() -> "Theatre of Blood Points", () -> 115.0),
    TITHE_FARM_POINTS(() -> "Tithe Farm Points", () -> 120.0),
    VOID_KNIGHT_COMMENDATION_POINTS(() -> "Void Knight Commendation Points", () -> 125.0),
    VOLCANIC_MINE_REWARD_POINTS(() -> "Volcanic Mine Reward Points", () -> 130.0),
    ZEAL_TOKENS(() -> "Zeal Tokens", () -> 135.0),

    // Additional Currencies
    DUNGEONEERING_TOKENS(() -> "Dungeoneering Tokens", () -> 140.0),
    RUNE_POINTS(() -> "Rune Points", () -> 145.0),
    FARMING_POINTS(() -> "Farming Points", () -> 150.0),
    CONSTRUCTION_POINTS(() -> "Construction Points", () -> 155.0),
    SUMMONING_POINTS(() -> "Summoning Points", () -> 160.0),
    ABYSSAL_SIRE_POINTS(() -> "Abyssal Sire Points", () -> 165.0),
    INFERNO_POINTS(() -> "Inferno Points", () -> 170.0),
    KOUREND_POINTS(() -> "Kourend Points", () -> 175.0),
    SOULS_REAPER_POINTS(() -> "Souls Reaper Points", () -> 180.0);

    private final Supplier<String> displayNameSupplier; // Supplier for the currency name
    private final Supplier<Double> conversionRateSupplier; // Supplier for the conversion rate

    // Getters for the currency name and conversion rate
    public String getDisplayName() {
        return displayNameSupplier.get();
    }

    public double getConversionRate() {
        return conversionRateSupplier.get();
    }
}
