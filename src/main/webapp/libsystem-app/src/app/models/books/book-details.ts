import { YearOfPrint } from "../book-properties/year-of-print";
import { Affiliate } from "../common-book-properties/affiliate";
import { Author } from "../common-book-properties/author";
import { Genre } from "../common-book-properties/genre";
import { Publisher } from "../common-book-properties/publisher";
import { AffiliateBook } from "../book-properties/affiliate-book";

export interface BookDetails {
    title: string;
    publisherEntity: Publisher;
    yearOfPrintEntity: YearOfPrint;
    genres: Genre[];
    affiliates: Affiliate[];
    description: string;
    authors: Author[];
    affiliateBooks: AffiliateBook[];
}
