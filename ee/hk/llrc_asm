; HOTKEY Last line recall   V2.00     1988   Tony Tebby   QJUMP

        section hotkey

        xdef    hk_llrc

        include 'dev8_keys_k'
        include 'dev8_keys_sys'
        include 'dev8_keys_qu'
        include 'dev8_keys_qdos_sms'

;+++
; This routine does last line recall on current keyboard queue
;
;       status return 0
;---
hk_llrc
reglist reg     d1/d2/a0/a1/a2/a3
        movem.l reglist,-(sp)
        move.w  sr,d0                    ; save status
        trap    #0
        move.w  d0,-(sp)
        or.w    #$0700,sr                ; no interruptions please

        moveq   #sms.info,d0
        trap    #do.sms2

        move.l  sys_ckyq(a0),a2          ; set keyboard queue pointer
        move.l  qu_nexti(a2),d2          ; save next in
        move.l  d2,a1
        lea     qu_strtq(a2),a3          ; set start of queue pointer

        bsr.s   hkl_bline                ; back to beginning of line
        move.l  a1,qu_nexti(a2)
        bsr.s   hkl_bline                ; back to beginning of the prev line
        move.l  d1,qu_nexto(a2)          ; nxtout after <NL>

        move.w  (sp)+,sr
        moveq   #0,d0
        movem.l (sp)+,reglist
        rts

hkl_bline
hkl_bloop
        move.l  a1,d1                    ; keep current pointer
        cmp.l   a3,a1                    ; are we at start of queue buffer?
        bgt.s   hkl_bnext                ; ... no, back to next
        move.l  qu_endq(a2),a1           ; ... yes, reset to end
hkl_bnext
        cmp.b   #k.nl,-(a1)              ; ... back one and check
        beq.s   hkl_rts                  ; ... it is <NL>, done
        cmp.l   d2,a1                    ; have we gone all the way round?
        bne.s   hkl_bloop                ; ... no, carry on
hkl_rts
        rts
        end
