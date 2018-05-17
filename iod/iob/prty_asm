; IO Buffering parity and table  V2.00     1989  Tony Tebby   QJUMP

        section iou

        xdef    iob_prts
        xdef    iob_prtc
        xdef    iob_prty

        include 'dev8_keys_err'
;+++
; IO Buffering set parity
;
;       d0 cr   parity code 2=odd, 4=even, 6=mark 8=space / status OK
;       d1 cr   byte to set parity of
;
;       This is a clean routine
;
;       status return OK
;---
iob_prts
        add.w   ips_tab(pc,d0.w),d0
        jmp     ips_tab(pc,d0.w)
ips_tab
        dc.w    ips_exit-*
        dc.w    ips_odd-*
        dc.w    ips_even-*
        dc.w    ips_mark-*
        dc.w    ips_space-*

ips_odd
        and.w   #$007f,d1               ; seven bit
        move.b  iob_prty(pc,d1.w),d0    ; odd already?
        beq.s   ips_mark                ; ... no, make it odd
        bra.s   ips_exit

ips_even
        and.w   #$007f,d1                ; seven bit
        move.b  iob_prty(pc,d1.w),d0     ; even already?
        beq.s   ips_exit                ; ... yes

ips_mark
        bset    #7,d1                    ; set msb 
        bra.s   ips_exit

ips_space
        bclr    #7,d1                    ; clear msb

ips_exit
        moveq   #0,d0
        rts

;+++
; IO Buffering check parity
;
;       d0 cr   parity code 2=odd, 4=even, 6=mark 8=space / status 0 or parity
;       d1 cr   byte to set parity of
;
;       This is a clean routine
;
;       status return standard
;---
iob_prtc
        add.w   ipc_tab(pc,d0.w),d0
        jmp     ipc_tab(pc,d0.w)
ipc_tab
        dc.w    ipc_exok-*
        dc.w    ipc_odd-*
        dc.w    ipc_even-*
        dc.w    ipc_mark-*
        dc.w    ipc_space-*

ipc_odd
        and.w   #$00ff,d1
        move.b  iob_prty(pc,d1.w),d0
        bne.s   ipc_7bit                 ; ... odd
        bra.s   ipc_rxer                 ; ... not odd

ipc_even
        and.w   #$00ff,d1
        move.b  iob_prty(pc,d1.w),d0
        beq.s   ipc_7bit                 ; ... even
        bra.s   ipc_rxer                 ; ... not even

ipc_mark
        bclr    #7,d1                    ; seven bit now
        bne.s   ipc_exok                 ; ... ok
        bra.s   ipc_rxer                 ; ... bit seven not set

ipc_space
        bclr    #7,d1                    ; seven bit now
        beq.s   ipc_exok                 ; ... ok

ipc_rxer
        moveq   #err.prty,d0             ; parity error
        rts

ipc_7bit
        bclr    #7,d1                    ; seven bit data
ipc_exok
        moveq   #0,d0
        rts

.       equ    $ff
;+++
; IO Buffering parity table. $00=even, $ff=odd
;---
iob_prty
        dc.b    0,.,.,0,.,0,0,.,.,0,0,.,0,.,.,0
        dc.b    .,0,0,.,0,.,.,0,0,.,.,0,.,0,0,.
        dc.b    .,0,0,.,0,.,.,0,0,.,.,0,.,0,0,.
        dc.b    0,.,.,0,.,0,0,.,.,0,0,.,0,.,.,0
        dc.b    .,0,0,.,0,.,.,0,0,.,.,0,.,0,0,.
        dc.b    0,.,.,0,.,0,0,.,.,0,0,.,0,.,.,0
        dc.b    0,.,.,0,.,0,0,.,.,0,0,.,0,.,.,0
        dc.b    .,0,0,.,0,.,.,0,0,.,.,0,.,0,0,.
        dc.b    .,0,0,.,0,.,.,0,0,.,.,0,.,0,0,.
        dc.b    0,.,.,0,.,0,0,.,.,0,0,.,0,.,.,0
        dc.b    0,.,.,0,.,0,0,.,.,0,0,.,0,.,.,0
        dc.b    .,0,0,.,0,.,.,0,0,.,.,0,.,0,0,.
        dc.b    0,.,.,0,.,0,0,.,.,0,0,.,0,.,.,0
        dc.b    .,0,0,.,0,.,.,0,0,.,.,0,.,0,0,.
        dc.b    .,0,0,.,0,.,.,0,0,.,.,0,.,0,0,.
        dc.b    0,.,.,0,.,0,0,.,.,0,0,.,0,.,.,0
        end
