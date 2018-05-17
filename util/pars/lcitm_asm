; Locate item in line                     V2.00   1989  Tony Tebby

        section pars

        xdef    ps_lcitm

        include 'dev8_keys_err'
        include 'dev8_keys_k'

;+++
; Locate next item in line. Skips to non-space, locates next space / null.
;
;       Registers:
;               Entry                           Exit
;       D0                                      0 length of item
;
;       A1      pointer to command line         next non space
;
;       Status as D0
;---
ps_lcitm
        move.l  a0,-(sp)                 ; save a reg
        moveq   #' ',d0
pli_sksp
        cmp.b   (a1)+,d0                 ; skip to non-space
        beq.s   pli_sksp
        subq.l  #1,a1
        move.l  a1,a0

pli_lcend
        cmp.b   (a0)+,d0                 ; end yet?
        blo.s   pli_lcend
        subq.l  #1,a0                    ; at end
        sub.l   a1,a0
        move.l  a0,d0
        move.l  (sp)+,a0
        rts
        end
