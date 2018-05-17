; HOTKEY set stuffer buffer   V2.02     1988   Tony Tebby   QJUMP

        section hotkey

        xdef    hk_sstbf                  ; set stuff buffer

        xref    cv_locas

        include 'dev8_ee_hk_data'
        include 'dev8_mac_assert'
;+++
; Set a new string in the stuffer buffer. It does not stuff a new string
; if this is the same as the previous string.
;
;       d2 c  p (word) number of characters to stuff
;       a1 c  p pointer to characters to stuff
;       a3 c  p linkage block
;       status return 0
;---
hk_sstbf
reglist reg     d1/d2/a1/a2/a4
stk_strl equ    $04
stk_str equ     $08
        movem.l reglist,-(sp)
        move.l  hkd_bpnt(a3),a2          ; current stuffer pointer

hsb_fmatch
        subq.w  #1,d2
        blt.s   hsb_match                ; run out of stuff string

        move.b  (a2)+,d1                 ; next character to match
        beq.s   hsb_set                  ; run out of string in buffer
        jsr     cv_locas
        move.b  d1,d0
        move.b  (a1)+,d1                 ; this matches?
        jsr     cv_locas
        cmp.b   d0,d1
        beq.s   hsb_fmatch               ; not the end of match yet
        bra.s   hsb_fend                 ; ... end of match, look for end

hsb_match
        tst.b   (a2)+                    ; length the same?
        beq.s   hsb_done                 ; ... yes, forget it

hsb_fend
        tst.b   (a2)+                    ; find end
        bne.s   hsb_fend                 ; ... not there yet

hsb_set
        move.l  stk_str(sp),a1           ; reset string pointer
        move.l  stk_strl(sp),d2          ; and length
        move.l  hkd_btop(a3),d0
        lea     (a2,d2.w),a4             ; end of new string
        cmp.l   d0,a4                    ; off end of buffer?
        blt.s   hsb_fbuf                 ; ... no, fill buffer

        move.l  hkd_bbas(a3),a2          ; start again at beginning
        sub.l   a2,d0                    ; max length +1
        cmp.w   d0,d2
        blt.s   hsb_fbuf                 ; ... ok, fill it
        move.w  d0,d2
        subq.w  #1,d2

hsb_fbuf
        move.l  a2,hkd_bpnt(a3)          ; set new pointer
        move.l  a2,hkd_ppnt(a3)          ; and new previous pointer
        bra.s   hsb_fbend

hsb_fblp
        move.b  (a1)+,(a2)+              ; copy character
hsb_fbend
        dbra    d2,hsb_fblp

hsb_fbclr
        tst.b   (a2)                     ; clear up to 
        sf      (a2)+                    ; clear
        bne.s   hsb_fbclr
hsb_done
        moveq   #0,d0
        movem.l (sp)+,reglist
        rts
        end
