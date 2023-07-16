import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class PaginationService {

  constructor() { }

  public getFirstItemIndex(currentPage: number, itemsPerPage: number): number {
    return (currentPage - 1) * itemsPerPage;
  }

  public getLastItemIndex(currentPage: number, itemsPerPage: number, items: any): number {
    const lastIndex = currentPage * itemsPerPage - 1;
    return Math.min(lastIndex, items.length - 1);
  }

  public getTotalPages(items: any, itemsPerPage: number): number {
    return Math.ceil(items.length / itemsPerPage);
  }

}
