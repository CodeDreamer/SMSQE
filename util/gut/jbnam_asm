; Fill in Job name              V0.00   1989  Tony Tebby

        section gen_util

        xdef    gu_jbnam

        xref    met_anon
        xref    met_nojb
        xref    met_sbas

        include 'dev8_keys_qdos_sms'

;+++
; Fill Job name into buffer.
;
;       Registers:
;               Entry                           Exit
;       D0                                      0 or error code
;       D1      Job ID                          preserved
;       D2      max room in buffer              preserved
;       A0      pointer to buffer               updated
*---
gu_jbnam
gjn.ereg reg     d1/d2/d3/a1/a2
gjn.ireg reg     d1/d2
gjn.xreg reg     d3/a1/a2
        movem.l gjn.ereg,-(sp)

        move.l  a0,a2
        moveq   #0,d2                   ; with job 0 at top of tree
        moveq   #sms.injb,d0            ; get some info on job
        trap    #do.sms2
        exg     a0,a2
        movem.l (sp)+,gjn.ireg
        tst.l   d0                      ; did it exist?
        bne.s   gjn_njob                ; no, no job


        addq.w  #6,a2                   ; set pointer to name flag
        cmp.w   #$4afb,(a2)+            ; name flag? $$$$
        beq.s   gjn_cpnm                ; yes, copy it
        lea     met_anon,a2             ; no, call it '*** Anon ***'
        tst.l   d1                      ; but is it SuperBASIC?
        bne.s   gjn_cpnm                ; no
        lea     met_sbas,a2             ; yes, call it that
        bra.s   gjn_cpnm
gjn_njob
        lea     met_nojb,a2

gjn_cpnm
        move.w  (a2)+,d3                ; get actual length
        cmp.w   d2,d3
        ble.s   gjn_cpce
        move.w  d2,d3
        bra.s   gjn_cpce
gjn_cpcl
        move.b  (a2)+,(a0)+             ; copy characters of name
gjn_cpce
        dbra    d3,gjn_cpcl

        tst.l   d0                      ; set error code
        movem.l (sp)+,gjn.xreg
        rts

        end
