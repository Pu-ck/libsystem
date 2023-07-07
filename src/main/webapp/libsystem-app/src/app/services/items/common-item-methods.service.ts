import { Injectable } from '@angular/core';
import { ParamMap, ActivatedRoute } from '@angular/router';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CommonItemMethodsService {

  private id: string = '';

  constructor(
    private route: ActivatedRoute
  ) { }

  public getItemId(): string {
    let id: string = '';
    this.route.paramMap.subscribe(params => {
      id = params.get('id') || '';
    });
    console.log(id);
    return id;
  }

  public getItemByParam(param: string): Observable<string> {
    return this.route.paramMap.pipe(
      map((params: ParamMap) => String(params.get(param)!))
    );
  }


}
