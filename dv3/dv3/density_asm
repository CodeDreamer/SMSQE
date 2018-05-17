; DV3 Decode Density Character    V3.00           1992 Tony Tebby

        section dv3

        xdef    dv3_density

;+++
; This routine converts the character in d0 to a density code in d2
;
;       d0 c  u character, returned with bit 5 cleared
;       d2  r   density code 0=S...3=E (-1.w if not real density)
;
;       Status return zero if real density, otherwise non zero
;---
dv3_density
        move.l  a1,-(sp)
        moveq   #3,d2
        and.b   #$df,d0                  ; upper case normal characters
        lea     ddn_list,a1
ddn_loop
        cmp.b   (a1)+,d0                 ; this one?
        dbeq    d2,ddn_loop

        move.l  (sp)+,a1
        rts

ddn_list dc.w   'EHDS'

        end
