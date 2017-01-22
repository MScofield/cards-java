package IronYard;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {

//    build a deck
    static HashSet<Card> createDeck() {
        HashSet<Card> deck = new HashSet();

        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                Card c = new Card(suit, rank);
                deck.add(c);
            }
        }
        return deck;
    }

//    sort out deck into 4-card hands
    static HashSet<HashSet<Card>> createHands(HashSet<Card> deck) {
        HashSet<HashSet<Card>> hands = new HashSet<>();
        for (Card c1 : deck) {
            HashSet<Card> deck2 = (HashSet<Card>) deck.clone(); //clone clones deck
            deck2.remove(c1);
            for (Card c2 : deck2) {
                HashSet<Card> deck3 = (HashSet<Card>) deck2.clone();
                deck3.remove(c2);
                for (Card c3 : deck3) {
                    HashSet<Card> deck4 = (HashSet<Card>) deck3.clone();
                    deck4.remove(c3);
                    for (Card c4 : deck4) {
                        HashSet<Card> hand = new HashSet();
                        hand.add(c1);
                        hand.add(c2);
                        hand.add(c3);
                        hand.add(c4);
                        hands.add(hand);
                    }
                }
            }
        }
        return hands;
    }

//      filter-methods in descending order of porousness
    static boolean isFlush(HashSet<Card> hand) {
        HashSet<Card.Suit> suits = hand.stream()
                .map(card -> {
                    return card.suit;
                })
                .collect(Collectors.toCollection(HashSet::new));
        return suits.size() == 1;
    }

    static boolean isTwoPair(HashSet<Card> hand) {
        TreeMap<Integer, Integer> freak = createFreak(hand);
        return ((freak.size() == 2) && (freak.containsValue(2)));

    }

    static boolean isStraight (HashSet<Card> hand) {
        HashSet<Card.Rank> ranks = hand.stream()
                .map(card -> {
                    return card.rank;
                })
                .collect(Collectors.toCollection(HashSet<Card.Rank>::new));
        if (ranks.size() == 4) {
            ArrayList<Integer> hogpile = new ArrayList<>();
            ranks.forEach(rank -> hogpile.add(rank.ordinal()));
            Collections.sort(hogpile);
            return ((hogpile.get(hogpile.size() - 1) - hogpile.get(0)) == 3);
        }   else {
            return false;
        }
    }

    static boolean isThreeKind(HashSet<Card> hand) {
        TreeMap<Integer, Integer> freak = createFreak(hand);
        return freak.containsValue(3);
    }

    static boolean isStraightFlush(HashSet<Card> hand) {
        return isFlush(hand) && isStraight(hand);
    }

    static boolean isFourKind(HashSet<Card> hand) {
        TreeMap<Integer, Integer> freak = createFreak(hand);
        return freak.size() == 1;
    }

//    freaky little frequency map making order of the natural chaos
    public static TreeMap<Integer, Integer> createFreak(HashSet<Card> hand) {
        TreeMap<Integer, Integer> freak = new TreeMap<>();
        for (Card z : hand) {
            Integer stable = freak.get(z.rank.ordinal());
            freak.put(z.rank.ordinal(), (stable == null) ? 1 : stable + 1);
        }
        return freak;
    }

//    consolidate all the counting blocks that were once in main squeeze
    public static Integer rainMan(HashSet<HashSet<Card>> noseTap, Function<HashSet<Card>, Boolean> method) {
        noseTap = noseTap.stream()
                .filter((hand) -> {
            return method.apply(hand);
                })
                .collect(Collectors.toCollection(HashSet<HashSet<Card>>::new));
        return noseTap.size();
    }

//      kick it all off and print results in descending order by volume /  dominance
    public static void main(String[] args) {
        HashSet<Card> fullDeck = createDeck();
        HashSet<HashSet<Card>> allHands = createHands(fullDeck);
        System.out.println("Four-of-a-kind  : " + rainMan(allHands, Main::isFourKind));
        System.out.println("Straight-Flush  : " + rainMan(allHands, Main::isStraightFlush));
        System.out.println("Three-of-a-kind : " + rainMan(allHands, Main::isThreeKind));
        System.out.println("Straight        : " + rainMan(allHands, Main::isStraight));
        System.out.println("Two-pair        : " + rainMan(allHands, Main::isTwoPair));
        System.out.println("Flush           : " + rainMan(allHands, Main::isFlush));
        System.out.println("total           : " + allHands.size());

    }

}
