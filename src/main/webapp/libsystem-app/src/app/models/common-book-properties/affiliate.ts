import { CommonBookProperty } from "./common-book-property";

export interface Affiliate extends CommonBookProperty {
    id: number;
    address: string;
    phoneNumber: string;
}
