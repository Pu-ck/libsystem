export interface User {
    id: string;
    username: string;
    cardNumber: number;
    firstName: string;
    lastName: string;
    enabled: boolean;
    orderedBooks: number;
    borrowedBooks: number;
    userType: string;
}
