; HOTKEY get stuffer buffer   V2.00     1988   Tony Tebby   QJUMP

        section hotkey

        xdef    hk_getpr
        xdef    hk_getbf

        include 'dev8_ee_hk_data'
        include 'dev8_mac_assert'

;+++
; Get stuffer buffer contents. This can get thus current string (d0=0)
; or the previous string (d0=-1)
;
;       d0 cr   (byte) key, 0=current string, -1=previous string
;       d2  r   length of string
;       a1  r   pointer to characterrs
;       a3 c  p linkage block
;       status return 0
;---
hk_getbf
        tst.b   d0                       ; this or previous?
        beq.s   hgb_cur
        bsr.s   hk_getpr                 ; previous
        bra.s   hgb_do

hgb_cur
        move.l  hkd_bpnt(a3),hkd_ppnt(a3); reset previous pointer to string
hgb_do
        move.l  hkd_ppnt(a3),a1
        move.l  a1,d0
        beq.s   hgb_done

hgb_look
        tst.b   (a1)+                    ; look for end
        bne.s   hgb_look

        subq.l  #1,a1                    ; this is it
hgb_done
        move.l  a1,d2                    ; end
        move.l  d0,a1                    ; start
        sub.l   a1,d2                    ; length
hg_rts0
        moveq   #0,d0
        rts

;+++
; Get previous stuffer string. This just backspaces the stuffer buffer pointers.
;
;       a3 c  p linkage block
;       status return 0
;---
hk_getpr
        move.l  a2,-(sp)
        move.l  hkd_bbas(a3),d0          ; buffer base
        beq.s   hgp_exit
        move.l  hkd_ppnt(a3),a2          ; buffer pointer
        cmp.l   d0,a2                    ; at start?
        bne.s   hgp_look
        move.l  hkd_btop(a3),a2

hgp_look
        sub.l   a2,d0                    ; amount we can go back
        not.w   d0
hgp_lnz
        tst.b   -(a2)                    ; look for non zero
        dbne    d0,hgp_lnz 
        beq.s   hgp_found
        bra.s   hgp_zend
hgp_zloop
        tst.b   -(a2)                    ; look for zero
hgp_zend
        dbeq    d0,hgp_zloop
        bne.s   hgp_found
        addq.l  #1,a2

hgp_found
        move.l  a2,hkd_ppnt(a3)          ; reset pointer
hgp_exit
        move.l  (sp)+,a2
        moveq   #0,d0
        rts
        end
