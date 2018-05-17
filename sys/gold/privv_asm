; Gold 40 Priviledge Violation intercept.
;
        xdef    privv

privv_stop
        lea     pv_brah,a0
        move.l  a0,d0
        move.l  $e(sp),a0
        move.l  d0,$e(sp)               ; bra to myself
        bra.s   privv_ret

pv_brah bra.s   *
        dc.w    'Priv V at (A0)'

;+++
; Priviledge violation
;---
privv
        movem.l d0/d1/d2/a0,-(sp)
        move.l  $12(sp),a0              ; address of offending instruction
        moveq   #2,d2                   ; instruction length

        moveq   #$ffffffc0,d0
        and.w   (a0),d0                 ; mask all but data reg
        cmp.w   #$40c0,d0               ; move.w SR,ea?
        bne.s   privv_stop

        dc.l    $4e7a0002               ; cache control to d0
        bset    #3,d0                   ; set clear cache bit
        dc.l    $4e7b0002               ; d0 to cache control

        moveq   #7,d1
        and.w   (a0),d1                 ; register
        moveq   #$38,d0
        and.w   (a0),d0                 ; mode
        beq.s   pv_movesr               ; ... data reg
        cmp.b   #$38,d0                 ; absolute?
        beq.s   pv_movea                ; ... yes

        cmp.b   #$28,d0                 ; displacement?
        blt.s   pv_cka7                 ; ... no
        beq.s   pv_4byte                ; ... 16 bit
        btst    #0,2(a0)                ; MC680x0 instruction?
        bne.s   privv_stop              ; ... yes

pv_4byte
        moveq   #4,d2                   ; ... four byte instruction

pv_cka7
        cmp.b   #7,d1                   ; sr onto user stack?
        bne.s   pv_movesr

        move    usp,a0                  ; use a0 instead
        lsl.w   #3,d0                   ; destination (a0) in word
        swap    d0
        or.l    movesr,d0               ; move.w $1a(sp),...(a0....)
        move.w  #$4e75,-(sp)            ; rts
        move.l  d0,-(sp)

        jsr     (sp)                    ; transfer to ea

        move    a0,usp                  ; --- in case it was -(sp) or (sp)+
        addq.l  #$6,sp
        add.l   d2,$12(sp)
privv_ret
        movem.l (sp)+,d0/d1/d2/a0          ; restore regs
        rte

pv_movea
        add.w   d1,d2                   ; d2= 2 or 3
        add.w   d2,d2                   ; length of instruction

pv_movesr
        lsl.w   #6,d1                   ; swap reg/mode
        or.w    d1,d0
        lsl.w   #3,d0                   ; destination in word
        swap    d0
        or.l    movesr,d0               ; move.w $1a(sp),ea
        move.w  #$4e75,-(sp)            ; rts
        move.l  d0,-(sp)
        movem.l $6(sp),d0/d1/d2/a0      ; restore regs
        jsr     (sp)                    ; transfer to ea
        add.w   #$16,sp
        rte

movesr  move.w  $1a(sp),d0
        end
