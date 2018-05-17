; Substitute Extension in filename     V2.00    1990  Tony Tebby   QJUMP

        section cv

        xdef    cv_cinst

        include 'dev8_keys_err'

;+++
; Locate character in string
;
;       Registers:
;               Entry                           Exit
;       d0      pointer to start (0??)          pointer to match or err.itnf
;       d1      character to match              preserved
;       a1      pointer to string               preserved
;       all other registers preserved
;       status according to d0
;---
cv_cinst
        move.l  a1,-(sp)                 ; save string pointer
        sub.w   (a1),d0                  ; - number to check
        bge.s   cci_nf
        add.w   (a1)+,a1
        add.w   d0,a1                    ; starting here

        not.w   d0                       ; number to check-1
cci_loop
        cmp.b   (a1)+,d1                 ; check character
        dbeq    d0,cci_loop

        bne.s   cci_nf                   ; not found at end
        move.l  a1,d0                    ; where we got to (+1)
        move.l  (sp)+,a1                 ; where we started
        sub.l   a1,d0
        subq.l  #3,d0                    ; actual length
        rts

cci_nf
        move.l  (sp)+,a1
        moveq   #err.itnf,d0
        rts
        end
