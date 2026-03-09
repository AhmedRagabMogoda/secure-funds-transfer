import {
  trigger, transition, style, animate, query,
  stagger, keyframes, state
} from '@angular/animations';

/** Fade + slide up — used for page entry */
export const fadeSlideIn = trigger('fadeSlideIn', [
  transition(':enter', [
    style({ opacity: 0, transform: 'translateY(24px)' }),
    animate('420ms cubic-bezier(0.35, 0, 0.25, 1)',
      style({ opacity: 1, transform: 'translateY(0)' }))
  ])
]);

/** Staggered list items */
export const listStagger = trigger('listStagger', [
  transition('* => *', [
    query(':enter', [
      style({ opacity: 0, transform: 'translateX(-16px)' }),
      stagger('60ms', [
        animate('350ms ease-out',
          style({ opacity: 1, transform: 'translateX(0)' }))
      ])
    ], { optional: true })
  ])
]);

/** Card pop-in */
export const cardEnter = trigger('cardEnter', [
  transition(':enter', [
    style({ opacity: 0, transform: 'scale(0.96)' }),
    animate('380ms cubic-bezier(0.35, 0, 0.25, 1)',
      style({ opacity: 1, transform: 'scale(1)' }))
  ])
]);

/** Success pulse */
export const successPulse = trigger('successPulse', [
  transition(':enter', [
    animate('600ms ease-out', keyframes([
      style({ opacity: 0, transform: 'scale(0.8)', offset: 0 }),
      style({ opacity: 1, transform: 'scale(1.06)', offset: 0.6 }),
      style({ opacity: 1, transform: 'scale(1)',    offset: 1 })
    ]))
  ])
]);

/** Route-level page transition */
export const routeFade = trigger('routeFade', [
  transition('* <=> *', [
    query(':enter', [
      style({ opacity: 0, transform: 'translateY(16px)' }),
      animate('350ms ease-out', style({ opacity: 1, transform: 'translateY(0)' }))
    ], { optional: true })
  ])
]);
