package com.henri.poker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

public class PokerHand {
    public enum Result {TIE, WIN, LOSS}

    public Hand myHand = new Hand();

    public PokerHand(String hand) {
        String[] stringHand = hand.split(" ");

        for (int i = 0; i <stringHand.length ; i++)
            myHand.hand.add(new Card(stringHand[i]));

       Collections.sort(myHand.hand);

        assignRank(myHand);
        if(myHand.highCardValue == -1)
            assignHighCard(myHand);
        System.out.println("ok");
    }

    public Result compareWith(PokerHand other) {
        Hand opponent = other.myHand;

        if(opponent.rank > myHand.rank)
            return Result.LOSS;
        if(opponent.rank < myHand.rank)
            return Result.WIN;
        if(opponent.rank == myHand.rank){

            //TODO: 
            // find highest of matching cards

            // if equal, follow high-card rules
            if(opponent.highCardValue > myHand.highCardValue)
                return Result.LOSS;
            if(opponent.highCardValue < myHand.highCardValue)
                return Result.WIN;
            else
                return Result.TIE;
        }
        return Result.TIE;
    }

    public void assignRank(Hand aHand){
        int rank = 1;
        if(isOnePair(aHand))
            rank = 2;
        if(isTwoPair(aHand))
            rank = 3;
        if(isThreeOfAKind(aHand))
            rank = 4;
        if(isStraight(aHand))
            rank = 5;
        if(isFlush(aHand))
            rank = 6;
        if(isFullHouse(aHand))
            rank = 7;
        if(isFourOfAKind(aHand))
            rank = 8;
        if(isStraighFlush(aHand))
            rank = 9;
        aHand.rank = rank;
    }

    public void assignHighCard(Hand aHand){
        Iterator<Card> itr = aHand.hand.iterator();
        int maxValue = itr.next().value;
        while(itr.hasNext()){
            int newValue = itr.next().value;
            if(newValue < maxValue)
                maxValue = newValue;
        }
        aHand.highCardValue = maxValue;
    }
    public void assignHighCardFollowHCRules(Hand aHand, int valueNotIncluded){
        Iterator<Card> itr = aHand.hand.iterator();
        int minValue = -1;

        while(minValue == -1 && itr.hasNext()){
            int next = itr.next().value;
            if(next != valueNotIncluded)
                minValue = next;
        }

        itr = aHand.hand.iterator();

        while(itr.hasNext()){
            int newValue = itr.next().value;
            if(newValue < minValue && newValue != valueNotIncluded)
                minValue = newValue;
        }
        aHand.highCardValue = minValue;
    }

    public boolean isStraighFlush(Hand aHand){
        // all same suit
        Iterator<Card> itr = aHand.hand.iterator();
        int currentSuit = itr.next().suit;

        while(itr.hasNext()){
            if(currentSuit != itr.next().suit)
                return false;
        }
        // && in sequential order
        itr = aHand.hand.iterator();
        int lastValue = itr.next().value;

        while(itr.hasNext()){
            int nextValue = itr.next().value;
            if( (lastValue > nextValue) || ((nextValue - lastValue) != 1) )
                return false;
            lastValue = nextValue;
        }
        return true;
    }

    public boolean isFourOfAKind(Hand aHand){
        // four cards with same value, one non-matching.
        Iterator<Card> itr = aHand.hand.iterator();

        int[] occurrences = new int[15];
        Arrays.fill(occurrences, 0);

        while(itr.hasNext()){
            int currentValue = itr.next().value;
            int count = occurrences[currentValue];
            occurrences[currentValue] = ++count;
            //available indexes are 2 -> 14
        }

        boolean fourMatch = false;
        boolean oneNonMatch = false;

        for (int i = 0; i <occurrences.length ; i++) {
            if(occurrences[i] == 4)
                fourMatch = true;
            if(occurrences[i] == 1)
                oneNonMatch = true;
        }

        if(fourMatch && oneNonMatch)
            return true;

        // else
        return false;
    }

    public boolean isFullHouse(Hand aHand){
        // three cards of one face value and two of another
        Iterator<Card> itr = aHand.hand.iterator();

        int[] occurrences = new int[15];
        Arrays.fill(occurrences, 0);

        while(itr.hasNext()){
            int currentValue = itr.next().value;
            int count = occurrences[currentValue];
            occurrences[currentValue] = ++count;
            //available indexes are 2 -> 14
        }

        boolean threeMatch = false;
        boolean twoMatch = false;

        for (int i = 0; i <occurrences.length ; i++) {
            if(occurrences[i] == 3)
                threeMatch = true;
            if(occurrences[i] == 2)
                twoMatch = true;
        }

        if(threeMatch && twoMatch)
            return true;

        // else
        return false;
    }

    public boolean isFlush(Hand aHand){
        // All five cards are the same suit
        Iterator<Card> itr = aHand.hand.iterator();

        int suit = itr.next().suit;

        while (itr.hasNext()){
            if(itr.next().suit != suit)
                return false;
        }

        // else
        return true;
    }

    public boolean isStraight(Hand aHand){
        // Five cards in any sequential order
        Iterator<Card> itr = aHand.hand.iterator();

        int lastValue = itr.next().value;

        while(itr.hasNext()){
            int nextValue = itr.next().value;
            if( (lastValue > nextValue) || ((nextValue - lastValue) != 1) )
                return false;
            lastValue = nextValue;
        }

        //else
        return true;
    }

    public boolean isThreeOfAKind(Hand aHand){
        // three cards with the same value, and two non-matching cards
        Iterator<Card> itr = aHand.hand.iterator();

        int[] occurrences = new int[15];
        Arrays.fill(occurrences, 0);

        while(itr.hasNext()){
            int currentValue = itr.next().value;
            int count = occurrences[currentValue];
            occurrences[currentValue] = ++count;
            //available indexes are 2 -> 14
        }

        boolean threeMatch = false;
        int oneOccurCount = 0;

        for (int i = 0; i <occurrences.length ; i++) {
            if(occurrences[i] == 3)
                threeMatch = true;
            if(occurrences[i] == 1)
                ++oneOccurCount;
        }

        if(threeMatch && oneOccurCount == 2)
            return true;

        // else
        return false;
    }

    public boolean isTwoPair(Hand aHand){
        // Two sets of two matching cards, and a non-matching card, such as A,A,9,9,2.
        Iterator<Card> itr = aHand.hand.iterator();

        int[] occurrences = new int[15];
        Arrays.fill(occurrences, 0);

        while(itr.hasNext()){
            int currentValue = itr.next().value;
            int count = occurrences[currentValue];
            occurrences[currentValue] = ++count;
            //available indexes are 2 -> 14
        }

        int pairOccurCount = 0;

        for (int i = 0; i <occurrences.length ; i++) {
            if(occurrences[i] == 2)
                ++pairOccurCount;
        }

        if(pairOccurCount == 2)
            return true;

        // else
        return false;
    }

    public boolean isOnePair(Hand aHand){
        // One set of matching cards, and three non-matching cards,
        Iterator<Card> itr = aHand.hand.iterator();

        int[] occurrences = new int[15];
        Arrays.fill(occurrences, 0);

        while(itr.hasNext()){
            int currentValue = itr.next().value;
            int count = occurrences[currentValue];
            occurrences[currentValue] = ++count;
            //available indexes are 2 -> 14
        }

        for (int i = 0; i <occurrences.length ; i++) {
            if(occurrences[i] == 2){
                assignHighCardFollowHCRules(aHand, i);
                return true;
            }
        }

        // else
        return false;
    }

    // card class
    public class Card  implements Comparable<Card> {
        public int value;
        public int suit;

    public Card(String hand) {
            char[] valueAndSuit = hand.toCharArray();
            switch (valueAndSuit[0]) {
                case '2':
                    this.value = 2;
                    break;
                case '3':
                    this.value = 3;
                    break;
                case '4':
                    this.value = 4;
                    break;
                case '5':
                    this.value = 5;
                    break;
                case '6':
                    this.value = 6;
                    break;
                case '7':
                    this.value = 7;
                    break;
                case '8':
                    this.value = 8;
                    break;
                case '9':
                    this.value = 9;
                    break;
                case 'T':
                    this.value = 10;
                    break;
                case 'J':
                    this.value = 11;
                    break;
                case 'Q':
                    this.value = 12;
                    break;
                case 'K':
                    this.value = 13;
                    break;
                case 'A':
                    this.value = 14;
                    break;
            }
            switch (valueAndSuit[1]) {
                case 'S':
                    this.suit = 1;
                    break;
                case 'H':
                    this.suit = 2;
                    break;
                case 'D':
                    this.suit = 3;
                    break;
                case 'C':
                    this.suit = 4;
                    break;
            }
        } // constructor end

        @Override
        public int compareTo(Card o) {
        if(this.value < o.value)
            return -1;
        if(this.value > o.value)
            return 1;
        return 0;
        }
    } // Card class end

    public class Hand {
        public ArrayList<Card> hand;
        public int rank = -1;
        public int highCardValue = -1;
        public int matchingCardValue = -1;

        public Hand(){
            hand = new ArrayList<Card>();
        }
    }
}