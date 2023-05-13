import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class CommonBookMethodsService {

  constructor() { }

  public getAffiliateBookQuantity(book: any, affiliateId: number): string {
    const affiliateBook = book.affiliateBooks.find((ab: any) => ab.affiliateId === affiliateId);
    return affiliateBook ? `${affiliateBook.currentQuantity}/${affiliateBook.generalQuantity}` : '';
  }

}
