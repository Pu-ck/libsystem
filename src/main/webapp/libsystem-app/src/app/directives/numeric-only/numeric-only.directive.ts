import { Directive, ElementRef, HostListener } from '@angular/core';

@Directive({
  selector: '[appNumericOnly]'
})
export class NumericOnlyDirective {

  constructor(private elementRef: ElementRef) { }

  @HostListener('input', ['$event'])
  onInputChange(event: any) {
    const initialValue = this.elementRef.nativeElement.value;
    this.elementRef.nativeElement.value = initialValue.replace(/[^0-9]*/g, '');
    if (initialValue !== this.elementRef.nativeElement.value) {
      event.stopPropagation();
    }
  }

}
