; HOTKEY Extensions  V2.04       1988  Tony Tebby   QJUMP

        section base

        xref    hot_tkext
        xref    hot_init
        xref    hk_init
        xref    hk_sprc

        include 'dev8_ee_hk_data'

        bra.l   hot_start

        section version
        section language
        section procs
        section hotkey

hot_start
        jsr     hk_init                  ; initialise thing
        bne.s   hot_rts

        jsr     hot_init                 ; initialise HOTKEY system
        bne.s   hot_rts

        jsr     hot_tkext                ; replace toolkit II altkey
        move.l  a1,hkd_tk2x(a3)          ; and keep old routine address
        moveq   #0,d0

hot_rts
        rts
        end
