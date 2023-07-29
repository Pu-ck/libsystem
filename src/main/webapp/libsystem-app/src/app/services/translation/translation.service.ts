import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

@Injectable({
  providedIn: 'root'
})
export class TranslationService {

  constructor(
    private translate: TranslateService
  ) { }

  public translateGenre(genre: string): string {
    return this.translate.instant(`genres.${genre}`);
  }

  public translateStatus(status: string): string {
    return this.translate.instant(`statuses.${status}`);
  }

}
