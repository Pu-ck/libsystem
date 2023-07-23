import { Affiliate } from "../common-book-properties/affiliate";

export interface BorrowedBook {
    id: string;
    bookId: string;
    userId: string;
    cardNumber: string;
    borrowDate: string;
    returnDate: string;
    readyDate: string;
    penalty: string;
    affiliateEntity: Affiliate;
    ready: boolean;
    accepted: boolean;
    extended: boolean;
    closed: boolean;
}
