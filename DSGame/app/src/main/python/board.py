import os
import json
from card import *
from collections import Counter


COLORS = ["blue", "black", "yellow", "red", "green", "purple", "orange"]
EXTRA_CARD_COLORS = ["blue", "white","black", "yellow", "red", "green", "purple", "orange"]

class Board:
    def __init__(self, all_cards=None, spots=None, all_extra_cards=None):
        self.all_cards = all_cards
        self.spots = spots

        self.all_extra_cards = all_extra_cards
        self.extra_cards = dict.fromkeys(EXTRA_CARD_COLORS)

        self.score = 0


    def get_best_cards(self, color):
        """Calculates best card of a color depending on board state"""
        best_card = None
        best_cards = []

        highest_val = 0
        for card in self.all_cards[color].values():
            # calculate the value of the board with that card
            current_value = self.calculate_board_value(card)
            # compare values and save best card
            if current_value > highest_val:
                best_card = card
                highest_val = current_value
        best_cards.append(best_card)
        best_card = None

        board_costs = self.get_letters_and_costs()[1]

        lowest_cost = 100
        for card in self.all_cards[color].values():
            current_cost = sum((Counter(board_costs) + Counter(card.costs)).values())
            if current_cost < lowest_cost:
                best_card = card
                lowest_cost = current_cost
        best_cards.append(best_card)
        best_card = None

        high_score = -100
        for card in self.all_cards[color].values():
            current_score = self.calculate_board_value(card) - sum(
                (Counter(board_costs) + Counter(card.costs)).values())
            if current_score > high_score:
                best_card = card
                high_score = current_score
        best_cards.append(best_card)

        return best_cards


    def calculate_board_value(self, test_card=None):
        value = 0

        # set temporarily current position to None in case of previous existant val
        old_card = None
        if (test_card is not None) and (self.spots[test_card.card_color] is not None):
            old_card = self.spots[test_card.card_color]
            self.spots[test_card.card_color] = None

        # add values of all current board cards
        for card in self.spots.values():
            if card is None:
                break
            value += card.value

        # if repeated letters add extra points, also add test_card value
        board_letters = self.get_letters_and_costs()[0]
        if test_card is not None:
            letters_rep = Counter(board_letters) + Counter(test_card.letters)
            value += test_card.value
        else:
            letters_rep = Counter(board_letters)

        for rep_number in letters_rep.values():
            if rep_number > 1:
                value += rep_number - 1

        # add extra cards value
        for ext_card in self.extra_cards.values():
            if ext_card is not None:
                value += ext_card.value

        # reset old value of spot
        if old_card:
            self.spots[test_card.card_color] = old_card
        return value

    def set_card(self, color, card_id):
        card = self.all_cards[color][card_id]
        self.spots[color] = card
        self.score = self.calculate_board_value()

    def add_extra_card(self, color):
        self.extra_cards[color] = self.all_extra_cards[color]
        self.score = self.calculate_board_value()

    def remove_extra_card(self, color):
        del self.extra_cards[color]
        self.score = self.calculate_board_value()

    def get_letters_and_costs(self):
        """Iterate over board and count letters"""
        letters_rep = []
        costs = []
        for color in COLORS:
            if self.spots[color] is not None:
                letters_rep += Counter(self.spots[color].letters)
                costs += Counter(self.spots[color].costs)
        return letters_rep, costs

    def update_board(self, kt_board):
        self.score = kt_board.getScore()
        for color in COLORS:
            self.spots[color] = transform_card(kt_board.getSpots().get(color))
        for color in EXTRA_CARD_COLORS:
            self.extra_cards[color] = transform_card(kt_board.getExtraCards().get(color))


def init_board(path_all_cards):
    """Initialize board value"""
    path_all_cards = os.path.join(os.path.dirname(__file__), path_all_cards)

    # load cards
    cards = dict.fromkeys(COLORS)
    cd_extra_dict = dict.fromkeys(EXTRA_CARD_COLORS)

    for filename in os.listdir(path_all_cards):
        type = filename.split(".")[0].split("-")[1]
        if filename.endswith(".json"):
            json_name = os.path.join(path_all_cards, filename)
            with open(json_name, 'r', encoding="utf8") as card_file:
                card_dict = {}
                id = 0
                card_data = json.loads(card_file.read())

                if type in COLORS:
                    for card in card_data:
                        id+=1
                        card_dict[id] = Card(id, card['name'], card['costs'], card['letters'], card['value'], card['card_color'])
                    cards[type] = card_dict

                elif type == "extra":
                    for card in card_data:
                        print(card['card_color'])
                        cd_extra_dict[card['card_color']] = Card(1, card['name'], card['costs'], card['letters'], card['value'], card['card_color'])

    # set board vals
    all_cards = cards
    spots = dict.fromkeys(COLORS)

    board = Board(all_cards, spots, cd_extra_dict)

    return board