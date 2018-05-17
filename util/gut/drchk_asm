; Check Directory Name        V1.00   1989  Tony Tebby  QJUMP

        section gen_util

        include 'dev8_keys_hdr'
        include 'dev8_keys_chunk'
        include 'dev8_keys_fll'

        xdef    gu_drchk

        xref    gu_prlis
        xref    cv_lctab
        xref    cv_lostr

;+++
; Check directory
;
;       Registers:
;               Entry                   Exit
;
;       A1      pointer to file header in gut_fll chunk
;       A3      pointer to gu_mkfll control block
;
;       All registers preserved. Status returned Z acceptable
;---
gu_drchk
gdc.reg reg     d0/d1/d2/d3/a0/a1/a2/a4

        tst.b   gmf_dlst(a3)             ; real directory?
        bge.s   gdc_chek                 ; ... no, check
        cmp.b   #-1,hdr_type(a1)         ; directory type?
        rts

gdc_chek
        movem.l gdc.reg,-(sp)            ; save registers
        lea     cv_lctab,a0              ; ... lowercase table
        moveq   #'_',d3                  ; set end of section char

        lea     gmf_dnam(a3),a2          ; directory name
        move.w  (a2)+,d2

        lea     hdr_name(a1),a1          ; real file name
        move.w  (a1)+,d1
        sub.w   d2,d1                    ; amount left of real name
        ble.s   gdc_nd
        moveq   #0,d0
        move.l  a1,a4                    ; keep start pointer
        bra.s   gdc_clend

; compare all of directory name

gdc_cloop
        move.b  (a1)+,d0
        move.b  (a0,d0.w),d0             ; lowercased char in name
        cmp.b   (a2)+,d0                 ; the same as directory?
gdc_clend
        dbne    d2,gdc_cloop
        bne.s   gdc_nd                   ; ... no match

        move.w  d1,d2
        bra.s   gdc_slend
gdc_sloop
        cmp.b   (a1)+,d3                 ; underscore?
gdc_slend
        dbeq    d2,gdc_sloop
        bne.s   gdc_nd                   ; ... not found

        clr.b   -(a1)                    ; blat the underscore
        sub.l   a4,a1                    ; length of (sub-)directory name
        move.w  a1,-(a4)
        move.l  a4,a1
        jsr     cv_lostr

        move.l  gmf_dlst(a3),d0          ; directory list
        beq.s   gdc_slst                 ; ... none, set it

        move.l  d0,a1
        lea     gdc_cmpn,a0              ; compare names
        jsr     gu_prlis
        bra.s   gdc_exit

gdc_slst
        lea     -chk.hdrl-fll_fnam(a4),a1 ; start of our list
        move.l  a1,gmf_dlst(a3)

gdc_ok
        moveq   #0,d0
        bra.s   gdc_exit

gdc_nd
        moveq    #-1,d0                  ; set NZ
gdc_exit
        movem.l (sp)+,gdc.reg
        rts

gdc_cmpn
        movem.l a1/a4,-(sp)
        lea     fll_fnam(a1),a1          ; existing name
        move.w  (a4)+,d0                 ; this name
        cmp.w   (a1)+,d0
        bra.s   gdc_cnle
gdc_cnlp
        cmpm.b  (a4)+,(a1)+
gdc_cnle
        dbne    d0,gdc_cnlp              ; compare length and characters
        movem.l (sp)+,a1/a4
        beq.s   gdc_cnne
        moveq   #0,d0                    ; different is ok
        rts
gdc_cnne
        moveq   #-1,d0                   ; same is not
        rts
        end
