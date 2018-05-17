; HOTKEY SET routines   V2.01     1988   Tony Tebby   QJUMP

        section hotkey

        xdef    hk_set                   ; set / reset kotkey

        xref    hk_newst
        xref    hk_fitem

        include 'dev8_keys_err'
        include 'dev8_ee_hk_data'
        include 'dev8_ee_hk_vector'
        include 'dev8_mac_assert'

;+++
; Set or reset a Hotkey.
; This routine can reset the state of a Hotkey to on or off.
; It can reset the Hotkey character for a current hotkey.
; It can set a new Hotkey item
;
;       d0 cr   operation key (byte) -1, off, 0 on, +1 reset, +2 set
;       d1 cr   (word) new key (reset,set, d0=+1,+2)
;       a1 c  p pointer to key or name (off, on, reset, d0=-1,0,+1)
;       a1 c  p pointer to item (set d0=+2)
;       a3 c  p linkage block
;       status 0 OK
;              err.fdnf hotkey not found (off, on, reset)
;              err.fdiu hotkey in use (reset, set)
;---
hk_set
        cmp.b   #hks.set,d0              ; set new?
        beq.l   hk_newst                 ; ... yes
reglist reg     d2/d3/d4/a2
        movem.l reglist,-(sp)
        lea     hkd_tabl(a3),a2          ; hotkey table
        move.b  d0,d4                    ; which other action
        ble.s   hs_fitem                 ; on or off
        tst.b   (a2,d1.w)                ; in use?
        bne.s   hs_fdiu
        move.w  d1,d3                    ; keep it

hs_fitem
        jsr     hk_fitem                 ; find old item
        bne.s   hs_exit                  ; not found
        add.w   d1,a2
        assert  hks.off+1,hks.on,hks.rset-1
        tst.b   d4                       ; what was it again?
        blt.s   hs_off
        beq.s   hs_on
        sub.w   d1,d3                    ; other bit
        move.b  (a2),(a2,d3.w)           ; transfer
        clr.b   (a2)
        add.w   d3,a2                    ; and set pointer

hs_on
        tst.b   (a2)                     ; on already?
        bgt.s   hs_ok                    ; ... yes
        bra.s   hs_neg
hs_off
        tst.b   (a2)                     ; off already
        blt.s   hs_ok                    ; ... yes
hs_neg
        neg.b   (a2)
hs_ok
        moveq   #0,d0
hs_exit
        movem.l (sp)+,reglist
        rts
hs_fdiu
        moveq   #err.fdiu,d0
        bra.s   hs_exit
        end
