from google.protobuf.internal import enum_type_wrapper as _enum_type_wrapper
from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from typing import ClassVar as _ClassVar, Mapping as _Mapping, Optional as _Optional, Union as _Union

Attack: ActionType
DESCRIPTOR: _descriptor.FileDescriptor
Idle: ActionType
Left: ActionType
P1: Player
P2: Player
Right: ActionType

class Action(_message.Message):
    __slots__ = ["type"]
    TYPE_FIELD_NUMBER: _ClassVar[int]
    type: ActionType
    def __init__(self, type: _Optional[_Union[ActionType, str]] = ...) -> None: ...

class Actions(_message.Message):
    __slots__ = ["p1", "p2"]
    P1_FIELD_NUMBER: _ClassVar[int]
    P2_FIELD_NUMBER: _ClassVar[int]
    p1: Action
    p2: Action
    def __init__(self, p1: _Optional[_Union[Action, _Mapping]] = ..., p2: _Optional[_Union[Action, _Mapping]] = ...) -> None: ...

class GameInfo(_message.Message):
    __slots__ = ["p1", "p2"]
    P1_FIELD_NUMBER: _ClassVar[int]
    P2_FIELD_NUMBER: _ClassVar[int]
    p1: PlayerInfo
    p2: PlayerInfo
    def __init__(self, p1: _Optional[_Union[PlayerInfo, _Mapping]] = ..., p2: _Optional[_Union[PlayerInfo, _Mapping]] = ...) -> None: ...

class PlayerInfo(_message.Message):
    __slots__ = ["currentAction", "health", "player", "position"]
    CURRENTACTION_FIELD_NUMBER: _ClassVar[int]
    HEALTH_FIELD_NUMBER: _ClassVar[int]
    PLAYER_FIELD_NUMBER: _ClassVar[int]
    POSITION_FIELD_NUMBER: _ClassVar[int]
    currentAction: ActionType
    health: float
    player: Player
    position: Position
    def __init__(self, player: _Optional[_Union[Player, str]] = ..., position: _Optional[_Union[Position, _Mapping]] = ..., health: _Optional[float] = ..., currentAction: _Optional[_Union[ActionType, str]] = ...) -> None: ...

class Position(_message.Message):
    __slots__ = ["x", "y"]
    X_FIELD_NUMBER: _ClassVar[int]
    Y_FIELD_NUMBER: _ClassVar[int]
    x: float
    y: float
    def __init__(self, x: _Optional[float] = ..., y: _Optional[float] = ...) -> None: ...

class ActionType(int, metaclass=_enum_type_wrapper.EnumTypeWrapper):
    __slots__ = []

class Player(int, metaclass=_enum_type_wrapper.EnumTypeWrapper):
    __slots__ = []
