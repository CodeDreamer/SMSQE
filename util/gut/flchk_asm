; Check Filename Against Pattern             V1.00   1989  Tony Tebby  QJUMP

        section gen_util

        include 'dev8_keys_hdr'
        include 'dev8_keys_fll'

        xdef    gu_flchk

        xref    cv_lctab

;+++
; Check filename against pattern.
;
;       Registers:
;               Entry                   Exit
;
;       A1      pointer to file header
;       A3      pointer gu_mkfll control block
;
;       All registers preserved. Status returned Z if match.
;---
gu_flchk
gfc.reg reg     d0/d1/d2/d3/d4/a0/a1/a2/a3/a4

        movem.l gfc.reg,-(sp)            ; save registers
        lea     cv_lctab,a0              ; ... lowercase table
        moveq   #0,d1
        moveq   #'_',d3                  ; set end of section char
        cmp.b   #-1,hdr_type(a1)         ; directory?
        sne     d4

        lea     gmf_dnam(a3),a2          ; directory name
        move.w  (a2)+,a4
        add.l   a2,a4                    ; end of it

        lea     hdr_name(a1),a1          ; real file name
        move.w  (a1)+,a3
        add.l   a1,a3                    ; end of real name

; compare sections

gfc_cloop
        moveq   #0,d0
gfc_ckend
        cmp.l   a4,a2                    ; end of pattern?
        bge.s   gfc_exit                 ; ... yes, done
        cmp.b   (a2),d3                  ; dummy destination section
        bne.s   gfc_cfile                ; ... no
        tst.b   d4                       ; ... yes, directory?
        beq.s   gfc_exit                 ; ... yes
        move.l  a2,d0                    ; ... no, keep it
        addq.l  #1,a2                    ; ... and move on
        bra.s   gfc_ckend

gfc_csect
        cmp.l   a4,a2                    ; end of pattern?
        bge.s   gfc_exit                 ; ... yes
gfc_cfile
        cmp.l   a3,a1                    ; end of file name?
        bge.s   gfc_nf                   ; ... yes
        move.b  (a2)+,d2
        cmp.b   d2,d3                    ; end of pattern section?
        beq.s   gfc_ssect                ; ... yes, skip to end
        move.b  (a1)+,d1
        cmp.b   (a0,d1.w),d2             ; match?
        beq.s   gfc_csect                ; ... yes, carry on

        tst.l   d0                       ; dummy section?
        beq.s   gfc_nf                   ; ... no
        move.l  d0,a2                    ; back to start of section

gfc_ssect
        cmp.l   a3,a1                    ; end of filename?
        beq.s   gfc_nf
        cmp.b   (a1)+,d3                 ; '_'?
        bne.s   gfc_ssect                ; ... no

        bra.s   gfc_cloop                ; ... yes, next section

gfc_nf
        moveq   #-1,d0                   ; set NZ
gfc_exit
        movem.l (sp)+,gfc.reg
        rts
        end
