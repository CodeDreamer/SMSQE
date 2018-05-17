; Copy Uppercase and Space Fill Name   V1.00     1990   Tony Tebby   QJUMP

        section procs

        xdef    pr_upcpy

        xref    cv_upstr

        include 'dev8_keys_err'

;+++
; Copy uppercase and space fill name
;
;       d1 c  s max length of name
;       a1 cu   pointer to destination (preserved if name too long)
;       a2 cu   pointer to name (rounded up to word boundary)
;
;       Status return standard (err.inam if name is too long)
;---
pr_upcpy
        move.w  (a2)+,d0                 ; length
        cmp.w   d1,d0                    ; too long?
        bhi.s   puc_inam                 ; ... yes

        move.l  a1,-(sp)
        move.w  d0,(a1)+
puc_loop
        move.w  (a2)+,(a1)+              ; copy characters in words
        subq.w  #2,d0
        bgt.s   puc_loop

        move.l  (sp)+,a1

        jsr     cv_upstr                 ; uppercase string

        move.w  (a1)+,d0                 ; length
        add.w   d0,a1
        sub.w   d0,d1                    ; empty bit
        moveq   #' ',d0
        bra.s   puc_efill
puc_fill
        move.b  d0,(a1)+
puc_efill
        dbra    d1,puc_fill
        moveq   #0,d0
        rts

puc_inam
        moveq   #err.inam,d0
        rts
        end
