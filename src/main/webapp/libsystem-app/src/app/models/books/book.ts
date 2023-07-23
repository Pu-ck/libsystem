import { Affiliate } from "../common-book-properties/affiliate";
import { Author } from "../common-book-properties/author";
import { Genre } from "../common-book-properties/genre";
import { Publisher } from "../common-book-properties/publisher";
import { YearOfPrint } from "../book-properties/year-of-print";

export interface Book {
    id: number;
    borrowedBookId: number;
    status: string;
    title: string;
    borrowDate: string;
    returnDate: string;
    readyDate: string;
    penalty: string;
    affiliate: string;
    extended: boolean;
    closed: boolean;
    accepted: boolean;
    bookDetailsLink: string;
    authors: Author[];
    genres: Genre[];
    name: string;
    addDate: string;
    affiliates: Affiliate[];
    yearOfPrintEntity: YearOfPrint;
    publisherEntity: Publisher;
}
