* Uppercase keystroke     V1.02     1986  Tony Tebby   QJUMP
*
        section wman
*
        xdef    wm_upcas
*
        include dev8_keys_qdos_pt
        include dev8_keys_k
*
*       d0  r   error return (0)
*       d2 cr   keystroke
*       d4  r   event number or 0
*
*               all other registers preserved
*
wm_upcas
        moveq   #0,d4                    ; no event
        and.w   #$00ff,d2                ; ensure clean byte in word
        move.b  table(pc,d2.w),d2
        cmp.b   #k.wake,d2               ; ... control?
        bhi.s   wm_ok                    ; ... no
*
        move.b  event(pc,d2.w),d4       set event
wm_ok
        moveq   #0,d0
        rts
event   dc.b    0,0,16,17,18,19,20,21,22,0  ; no, hit, do, escape, ...
*
table
        dc.b    $00,$01,$02,$03,$04,$05,$06,$07
        dc.b    $08,$09,$02,$0B,$0C,$02,$0E,$0F
        dc.b    $10,$11,$12,$13,$14,$15,$16,$17
        dc.b    $18,$19,$1A,$03,$1C,$1D,$1E,$1F
        dc.b    $01,$21,$22,$23,$24,$25,$26,$27
        dc.b    $28,$29,$2A,$2B,$2C,$2D,$2E,$2F
        dc.b    $30,$31,$32,$33,$34,$35,$36,$37
        dc.b    $38,$39,$3A,$3B,$3C,$3D,$3E,$3F
        dc.b    $40,$41,$42,$43,$44,$45,$46,$47
        dc.b    $48,$49,$4A,$4B,$4C,$4D,$4E,$4F
        dc.b    $50,$51,$52,$53,$54,$55,$56,$57
        dc.b    $58,$59,$5A,$5B,$5C,$5D,$5E,$5F
        dc.b    $60,$41,$42,$43,$44,$45,$46,$47
        dc.b    $48,$49,$4A,$4B,$4C,$4D,$4E,$4F
        dc.b    $50,$51,$52,$53,$54,$55,$56,$57
        dc.b    $58,$59,$5A,$7B,$7C,$7D,$7E,$7F
        dc.b    $a0,$a1,$a2,$a3,$a4,$a5,$a6,$a7
        dc.b    $a8,$a9,$aA,$aB,$8C,$8D,$8E,$8F
        dc.b    $90,$91,$92,$93,$94,$95,$96,$97
        dc.b    $98,$99,$9A,$9B,$9C,$9D,$9E,$9F
        dc.b    $A0,$A1,$A2,$A3,$A4,$A5,$A6,$A7
        dc.b    $A8,$A9,$AA,$AB,$AC,$AD,$AE,$AF
        dc.b    $B0,$B1,$B2,$B3,$B4,$B5,$B6,$B7
        dc.b    $B8,$B9,$BA,$BB,$BC,$BD,$BE,$BF
        dc.b    $C0,$C1,$C2,$C3,$C4,$C5,$C6,$C7
        dc.b    $C8,$C9,$CA,$CB,$CC,$CD,$CE,$CF
        dc.b    $D0,$D1,$D2,$D3,$D4,$D5,$D6,$D7
        dc.b    $D8,$D9,$DA,$DB,$DC,$DD,$DE,$DF
        dc.b    $E0,$E1,$E2,$E3,$E4,$E5,$E6,$E7
        dc.b    $04,$07,$EA,$EB,$EC,$08,$EE,$EF
        dc.b    $F0,$06,$F2,$F3,$F4,$05,$F6,$F7
        dc.b    $F8,$F9,$FA,$FB,$FC,$FD,$FE,$FF
        end
